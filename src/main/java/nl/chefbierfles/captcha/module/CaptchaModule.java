package nl.chefbierfles.captcha.module;

import nl.chefbierfles.captcha.Plugin;
import nl.chefbierfles.captcha.models.constants.Permissions;
import nl.chefbierfles.captcha.models.menus.CaptchaMenu;
import nl.chefbierfles.captcha.module.base.BaseModule;
import org.apache.commons.lang3.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class CaptchaModule extends BaseModule {

    private static HashMap<UUID, CaptchaMenu> OPEN_CAPTCHA_MENUS = new HashMap<>();

    /*
    Open inventory
     */
    public static void openCaptchaMenu(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), () -> {
            CaptchaMenu captchaMenu = getCaptchaMenu(player.getUniqueId());
            Bukkit.getScheduler().runTask(Plugin.getInstance(), () -> {
                player.openInventory(captchaMenu.getInventory());
            });
        });
    }

    /*
    When player joins and should do a captcha
     */
    public static void onPlayerJoinHandler(Player player) {

        if (!isIsEnabled()) return;

        if (player.hasPermission(Permissions.PERMISSION_CAPTCHA_BYPASS)) return;

        DatabaseModule.getCaptchaData(player);
    }

    public static void playerJoinCallback(Date lastdate, Player player) {
        // If current date is later then expire date
        if (lastdate == null || new Date().after(DateUtils.addMonths(lastdate, 1))) {
            openCaptchaMenu(player);
        }
    }

    /*
    When player quits
     */
    public static void onPlayerQuitHandler(Player player) {
        if (!isIsEnabled()) return;

        removeCaptcha(player.getUniqueId());
    }

    /*
    When a player tries to click a inventory slot
     */
    public static boolean onInventoryClickHandler(InventoryClickEvent event) {

        if (!isIsEnabled()) return false;

        if (!(event.getWhoClicked() instanceof Player)) return false;

        if (event.getClickedInventory() == null) return false;

        if (!hasCaptcha(event.getWhoClicked().getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(event.getWhoClicked().getUniqueId());

        //Check inventory is a captcha Inventory
        if (!event.getClickedInventory().getTitle().equals(captchaMenu.getInventory().getTitle())) return false;

        ItemStack clickedItem = event.getCurrentItem();

        //Check if capatchaItem is clicked
        if (clickedItem.getItemMeta().getDisplayName() == captchaMenu.getInvalidItem().getItemMeta().getDisplayName()) {

            //Check of maximaal bereikt is
            if (captchaMenu.getMistakesMade() == captchaMenu.getMaxMistakes()) {
                ((Player) event.getWhoClicked()).kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                removeCaptcha(event.getWhoClicked().getUniqueId());
                return true;
            }

            captchaMenu.addMistake();
            updateCaptchaMenu((Player) event.getWhoClicked(), captchaMenu);
        }

        if (clickedItem.getItemMeta().getDisplayName() == captchaMenu.getCorrectItem().getItemMeta().getDisplayName()) {

            if (captchaMenu.replaceCorrectItem(event.getSlot(), (Player) event.getWhoClicked()) == 0) {
                //Captcha is done
                finishCaptcha(event.getWhoClicked().getUniqueId());

                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Je hebt de captcha succesvol afgerond.");
            }
        }

        return true;
    }

    /*
    When a player tries to speak
     */
    public static boolean onAsyncPlayerChatHandler(Player player) {

        if (!isIsEnabled()) return false;

        //Check if player still needs to do captcha
        if (!hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(captchaMenu)) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() > captchaMenu.getMaxInventoryClosed()) {
                Bukkit.getScheduler().runTask(Plugin.getInstance(), () -> {
                    player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                });
                return true;
            }
            captchaMenu.addInventoryClosed();
            openCaptchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to interact
     */
    public static boolean onPlayerInteractHandler(Player player) {

        if (!isIsEnabled()) return false;

        //Check if player still needs to do captcha
        if (!hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(captchaMenu)) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() > captchaMenu.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            captchaMenu.addInventoryClosed();
            openCaptchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to move
     */
    public static boolean onPlayerMoveHandler(Player player) {

        if (!isIsEnabled()) return false;

        //Check if player still needs to do captcha
        if (!hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(captchaMenu)) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() > captchaMenu.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            captchaMenu.addInventoryClosed();
            openCaptchaMenu(player);
        }

        return true;
    }

    /*
    Get menu reference for player
     */
    private static CaptchaMenu getCaptchaMenu(UUID uuid) {
        if (OPEN_CAPTCHA_MENUS.containsKey(uuid)) {
            //Update current inventory
            return OPEN_CAPTCHA_MENUS.get(uuid);
        } else {
            CaptchaMenu captchaMenu = new CaptchaMenu();
            OPEN_CAPTCHA_MENUS.put(uuid, captchaMenu);
            return captchaMenu;
        }
    }

    /*
    Update menu contents
     */
    private static void updateCaptchaMenu(Player player, CaptchaMenu captchaMenu) {
        captchaMenu.updateMenu(player);
        OPEN_CAPTCHA_MENUS.put(player.getUniqueId(), captchaMenu);
    }

    /*
    Check if player needs to do the captcha
     */
    private static boolean hasCaptcha(UUID uuid) {
        return OPEN_CAPTCHA_MENUS.containsKey(uuid);
    }

    /*
    Remove menu reference for player
     */
    private static void removeCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }

    /*
    Remove menu reference for player
    */
    private static void finishCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        //Zet database waarde om over een maand weer te controleren
        DatabaseModule.addCapatchaData(uuid, Calendar.getInstance().getTime());

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }
}
