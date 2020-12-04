package nl.chefbierfles.captcha.module;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.helpers.constants.Permissions;
import nl.chefbierfles.captcha.models.menus.CaptchaMenu;
import nl.chefbierfles.captcha.module.base.BaseModule;
import org.apache.commons.lang3.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class CaptchaModule extends BaseModule {

    private HashMap<UUID, CaptchaMenu> OPEN_CAPTCHA_MENUS = new HashMap<>();

    public CaptchaModule() {
        name = this.getClass().getName();
        isEnabled = true;
    }

    /*
    Open inventory
     */
    public void openCaptchaMenu(Player player) {
        CaptchaMenu captchaMenu = getCaptchaMenu(player);
        player.openInventory(captchaMenu.getInventory());
    }

    /*
    When player joins and should do a captcha
     */
    public void onPlayerJoinHandler(Player player) {

        if (!isEnabled()) return;

        if (player.hasPermission(Permissions.PERMISSION_CAPTCHA_BYPASS)) return;

        CompletableFuture.supplyAsync(() -> getModuleManager().getDatabaseModule().getCaptchaData(player))
                .thenAccept(lastDoneDate -> {
                    if (lastDoneDate == null || new Date().after(DateUtils.addMonths(lastDoneDate, 1))) {
                        openCaptchaMenu(player);
                    }
                });
    }

    /*
    When player quits
     */
    public void onPlayerQuitHandler(Player player) {
        if (!isEnabled()) return;

        removeCaptcha(player.getUniqueId());
    }

    /*
    When a player tries to click a inventory slot
     */
    public boolean onInventoryClickHandler(InventoryClickEvent event) {

        if (!isEnabled()) return false;

        if (!(event.getWhoClicked() instanceof Player)) return false;

        if (event.getClickedInventory() == null) return false;

        if (!hasCaptcha(event.getWhoClicked().getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu((Player) event.getWhoClicked());

        //Check inventory is a captcha Inventory
        if (event.getClickedInventory().getTitle() != captchaMenu.getInventory().getTitle()) return false;

        ItemStack clickedItem = event.getCurrentItem();

        //Check if capatchaItem is clicked
        if (clickedItem.getItemMeta().getDisplayName() == captchaMenu.getInvalidItem().getItemMeta().getDisplayName()) {

            //Check of maximaal bereikt is
            if (captchaMenu.getMistakesMade() >= captchaMenu.getMaxMistakes()) {
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
    public boolean onAsyncPlayerChatHandler(Player player) {

        if (!isEnabled()) return false;

        //Check if player still needs to do captcha
        if (!hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(player);

        //Check if menu is open
        if (player.getOpenInventory().getTitle() != captchaMenu.getInventory().getTitle()) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() >= captchaMenu.getMaxInventoryClosed()) {
                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Captcha.class), () -> {
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
    public boolean onPlayerInteractHandler(Player player) {

        if (!isEnabled()) return false;

        //Check if player still needs to do captcha
        if (!hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(player);

        //Check if menu is open
        if (player.getOpenInventory().getTitle() != captchaMenu.getInventory().getTitle()) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() >= captchaMenu.getMaxInventoryClosed()) {
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
    public boolean onPlayerMoveHandler(Player player) {

        if (!isEnabled()) return false;

        //Check if player still needs to do captcha
        if (!hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = getCaptchaMenu(player);

        //Check if menu is open
        if (player.getOpenInventory().getTitle() != captchaMenu.getInventory().getTitle()) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() >= captchaMenu.getMaxInventoryClosed()) {
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
    private CaptchaMenu getCaptchaMenu(Player player) {
        if (OPEN_CAPTCHA_MENUS.containsKey(player.getUniqueId())) {
            //Update current inventory
            return OPEN_CAPTCHA_MENUS.get(player.getUniqueId());
        } else {
            CaptchaMenu captchaMenu = new CaptchaMenu(player);
            OPEN_CAPTCHA_MENUS.put(player.getUniqueId(), captchaMenu);
            return captchaMenu;
        }
    }

    /*
    Update menu contents
     */
    private void updateCaptchaMenu(Player player, CaptchaMenu captchaMenu) {
        captchaMenu.updateMenu(player);
        OPEN_CAPTCHA_MENUS.put(player.getUniqueId(), captchaMenu);
    }

    /*
    Check if player needs to do the captcha
     */
    private boolean hasCaptcha(UUID uuid) {
        return OPEN_CAPTCHA_MENUS.containsKey(uuid);
    }

    /*
    Remove menu reference for player
     */
    private void removeCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }

    /*
    Remove menu reference for player
    */
    private void finishCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        //Zet database waarde om over een maand weer te controleren
        getModuleManager().getDatabaseModule().addCapatchaData(uuid, Calendar.getInstance().getTime());

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }
}
