package nl.chefbierfles.captcha.module;

import nl.chefbierfles.captcha.Plugin;
import nl.chefbierfles.captcha.models.constants.Permissions;
import nl.chefbierfles.captcha.models.menus.CapatchaMenu;
import nl.chefbierfles.captcha.module.base.BaseModule;
import org.apache.commons.lang3.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CapatchaModule extends BaseModule {

    private static HashMap<UUID, CapatchaMenu> openCapatchaMenus = new HashMap<>();

    /*
    Open inventory
     */
    public static void openCapatchaMenu(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                CapatchaMenu capatchaMenu = getCapatchaMenu(player.getUniqueId());
                Bukkit.getScheduler().runTask(Plugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.openInventory(capatchaMenu.getInventory());
                    }
                });
            }
        });
    }

    /*
    When player joins and should do a capatcha
     */
    public static void onPlayerJoinHandler(Player player) {

        if (!isIsEnabled()) return;

        if (player.hasPermission(Permissions.PERMISSION_CAPATCHA_BYPASS.toString())) return;

        DatabaseModule.getCapatchaData(player);
    }

    public static void playerJoinCallback(Date lastdate, Player player) {
        // If current date is later then expire date
        if (lastdate == null || new Date().after(DateUtils.addMonths(lastdate, 1))) {
            openCapatchaMenu(player);
        }
    }

    /*
    When player quits
     */
    public static void onPlayerQuitHandler(Player player) {
        if (!isIsEnabled()) return;

        removeCapatcha(player.getUniqueId());
    }

    /*
    When a player tries to click a inventory slot
     */
    public static boolean onInventoryClickHandler(InventoryClickEvent event) {

        if (!isIsEnabled()) return false;

        if (!(event.getWhoClicked() instanceof Player)) return false;

        if (event.getClickedInventory() == null) return false;

        if (!hasCapatcha(event.getWhoClicked().getUniqueId())) return false;

        CapatchaMenu capatchaMenu = getCapatchaMenu(event.getWhoClicked().getUniqueId());

        //Check inventory is a capatcha Inventory
        if (!event.getClickedInventory().getTitle().equals(capatchaMenu.getInventory().getTitle())) return false;

        ItemStack clickedItem = event.getCurrentItem();

        //Check if capatchaItem is clicked
        if (clickedItem.getItemMeta().getDisplayName() == capatchaMenu.getInvalidItem().getItemMeta().getDisplayName()) {

            //Check of maximaal bereikt is
            if (capatchaMenu.getMistakesMade() == capatchaMenu.getMaxMistakes()) {
                ((Player) event.getWhoClicked()).kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                removeCapatcha(event.getWhoClicked().getUniqueId());
                return true;
            }

            capatchaMenu.addMistake();
            updateCapatchaMenu((Player) event.getWhoClicked(), capatchaMenu);
        }

        if (clickedItem.getItemMeta().getDisplayName() == capatchaMenu.getCorrectItem().getItemMeta().getDisplayName()) {

            if (capatchaMenu.replaceCorrectItem(event.getSlot(), (Player) event.getWhoClicked()) == 0) {
                //Capatcha is done
                finishCapatcha(event.getWhoClicked().getUniqueId());

                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Je hebt de capatcha succesvol afgerond.");
            }
        }

        return true;
    }

    /*
    When a player tries to speak
     */
    public static boolean onAsyncPlayerChatHandler(Player player) {

        if (!isIsEnabled()) return false;

        //Check if player still needs to do capatcha
        if (!hasCapatcha(player.getUniqueId())) return false;

        CapatchaMenu capatchaMenu = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaMenu)) {
            //Re-open menu
            if (capatchaMenu.getInventoryClosed() > capatchaMenu.getMaxInventoryClosed()) {
                Bukkit.getScheduler().runTask(Plugin.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                    }
                });
                return true;
            }
            capatchaMenu.addInventoryClosed();
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to interact
     */
    public static boolean onPlayerInteractHandler(Player player) {

        if (!isIsEnabled()) return false;

        //Check if player still needs to do capatcha
        if (!hasCapatcha(player.getUniqueId())) return false;

        CapatchaMenu capatchaMenu = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaMenu)) {
            //Re-open menu
            if (capatchaMenu.getInventoryClosed() > capatchaMenu.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            capatchaMenu.addInventoryClosed();
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to move
     */
    public static boolean onPlayerMoveHandler(Player player) {

        if (!isIsEnabled()) return false;

        //Check if player still needs to do capatcha
        if (!hasCapatcha(player.getUniqueId())) return false;

        CapatchaMenu capatchaMenu = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaMenu)) {
            //Re-open menu
            if (capatchaMenu.getInventoryClosed() > capatchaMenu.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            capatchaMenu.addInventoryClosed();
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    Get menu reference for player
     */
    private static CapatchaMenu getCapatchaMenu(UUID uuid) {
        if (openCapatchaMenus.containsKey(uuid)) {
            //Update current inventory
            return openCapatchaMenus.get(uuid);
        } else {
            CapatchaMenu capatchaMenu = new CapatchaMenu();
            openCapatchaMenus.put(uuid, capatchaMenu);
            return capatchaMenu;
        }
    }

    /*
    Update menu contents
     */
    private static void updateCapatchaMenu(Player player, CapatchaMenu capatchaMenu) {
        capatchaMenu.updateMenu(player);
        openCapatchaMenus.put(player.getUniqueId(), capatchaMenu);
    }

    /*
    Check if player needs to do the capatcha
     */
    private static boolean hasCapatcha(UUID uuid) {
        return openCapatchaMenus.containsKey(uuid);
    }

    /*
    Remove menu reference for player
     */
    private static void removeCapatcha(UUID uuid) {
        if (!openCapatchaMenus.containsKey(uuid)) return;

        openCapatchaMenus.remove(uuid);
    }

    /*
    Remove menu reference for player
    */
    private static void finishCapatcha(UUID uuid) {
        if (!openCapatchaMenus.containsKey(uuid)) return;

        //Zet database waarde om over een maand weer te controleren
        DatabaseModule.addCapatchaData(uuid, Calendar.getInstance().getTime());

        openCapatchaMenus.remove(uuid);
    }
}
