package nl.chefbierfles.capatcha.module;

import nl.chefbierfles.capatcha.Plugin;
import nl.chefbierfles.capatcha.models.enums.Permissions;
import nl.chefbierfles.capatcha.models.inventories.CapatchaInventory;
import nl.chefbierfles.capatcha.module.base.BaseModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.*;

public class CapatchaModule extends BaseModule {

    private static HashMap<UUID, CapatchaInventory> openCapatchaMenus = new HashMap<>();

    /*
    Open inventory
     */
    public static void openCapatchaMenu(Player player) {
        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());
        Bukkit.getScheduler().runTask(Plugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.openInventory(capatchaInventory.getInventory());
            }
        });
    }

    /*
    When player joins and should do a capatcha
     */
    public static void onPlayerJoinHandler(Player player) {

        if (!isIsEnabled()) return;

        if (player.hasPermission(Permissions.PERMISSION_CAPATCHA_BYPASS.toString())) return;

        Instant lastInstant = DatabaseModule.getCapatchaData(player.getUniqueId());
        // If current date is later then expire date
        if (lastInstant == null || Instant.now().isAfter(lastInstant.plusSeconds(30))) {
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

        CapatchaInventory capatchaInventory = getCapatchaMenu(event.getWhoClicked().getUniqueId());

        if (event.getClickedInventory() == null) return false;

        //Check inventory is a capatcha Inventory
        if (!event.getClickedInventory().equals(capatchaInventory.getInventory())) return false;

        ItemStack clickedItem = event.getCurrentItem();

        //Check if capatchaItem is clicked
        if (clickedItem.getItemMeta().getDisplayName() == capatchaInventory.getInvalidItem().getItemMeta().getDisplayName()) {

            //Check of maximaal bereikt is
            if (capatchaInventory.getMistakesMade() == capatchaInventory.getMaxMistakes()) {
                ((Player) event.getWhoClicked()).kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                removeCapatcha(event.getWhoClicked().getUniqueId());
                return true;
            }

            capatchaInventory.addMistake();
            updateCapatchaMenu((Player) event.getWhoClicked(), capatchaInventory);
        }

        if (clickedItem.getItemMeta().getDisplayName() == capatchaInventory.getCorrectItem().getItemMeta().getDisplayName()) {

            if (capatchaInventory.replaceCorrectItem(event.getSlot(), (Player) event.getWhoClicked()) == 0) {
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

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaInventory)) {
            //Re-open menu
            if (capatchaInventory.getInventoryClosed() > capatchaInventory.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            capatchaInventory.addInventoryClosed();
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

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaInventory)) {
            //Re-open menu
            if (capatchaInventory.getInventoryClosed() > capatchaInventory.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            capatchaInventory.addInventoryClosed();
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

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaInventory)) {
            //Re-open menu
            if (capatchaInventory.getInventoryClosed() > capatchaInventory.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            capatchaInventory.addInventoryClosed();
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    Get menu reference for player
     */
    private static CapatchaInventory getCapatchaMenu(UUID uuid) {
        if (openCapatchaMenus.containsKey(uuid)) {
            //Update current inventory
            return openCapatchaMenus.get(uuid);
        } else {
            CapatchaInventory capatchaInventory = new CapatchaInventory();
            openCapatchaMenus.put(uuid, capatchaInventory);
            return capatchaInventory;
        }
    }

    /*
    Update menu contents
     */
    private static void updateCapatchaMenu(Player player, CapatchaInventory capatchaInventory) {
        capatchaInventory.updateMenu(player);
        openCapatchaMenus.put(player.getUniqueId(), capatchaInventory);
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

        //TODO: Zet database waarde om over een maand weer te controleren
        DatabaseModule.addCapatchaData(uuid, Instant.now());

        openCapatchaMenus.remove(uuid);
    }
}
