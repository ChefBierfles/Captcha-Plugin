package nl.chefbierfles.capatcha.module;

import nl.chefbierfles.capatcha.models.inventories.CapatchaInventory;
import nl.chefbierfles.capatcha.module.base.BaseModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CapatchaModule extends BaseModule {

    private static HashMap<UUID, CapatchaInventory> openCapatchaMenus = new HashMap<>();

    /*
    Open inventory
     */
    public static void openCapatchaMenu(Player player) {
        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());
        player.openInventory(capatchaInventory.getInventory());
    }

    /*
    When player joins and should do a capatcha
     */
    public static void onPlayerJoinHandler(Player player) {

        //TODO Check if capatcha is needed (Database)

        if(!isIsEnabled()) return;

        openCapatchaMenu(player);
    }

    /*
    When player quits
     */
    public static void onPlayerQuitHandler(Player player) {
        if(!isIsEnabled()) return;

        removeCapatcha(player.getUniqueId());
    }

    /*
    When a player tries to click a inventory slot
     */
    public static boolean onInventoryClickHandler(InventoryClickEvent event) {

        if(!isIsEnabled()) return false;

        CapatchaInventory capatchaInventory = getCapatchaMenu(event.getWhoClicked().getUniqueId());

        if (event.getClickedInventory() == null) return false;

        //Check inventory is a capatcha Inventory
        if (!event.getClickedInventory().equals(capatchaInventory.getInventory())) return false;

        if (!(event.getWhoClicked() instanceof Player)) return false;

        ItemStack clickedItem = event.getCurrentItem();

        //Check if capatchaItem is clicked
        if (clickedItem.equals(capatchaInventory.getInvalidItem())) {

            //Check of maximaal bereikt is
            if (capatchaInventory.getMistakesMade() == capatchaInventory.getMaxMistakes()) {
                ((Player) event.getWhoClicked()).kickPlayer("Te veel ongeldige pogingen!");
                removeCapatcha(event.getWhoClicked().getUniqueId());
                return true;
            }

            capatchaInventory.addMistake();
            updateCapatchaMenu((Player) event.getWhoClicked(), capatchaInventory);
        }

        if (clickedItem.equals(capatchaInventory.getCorrectItem())) {

            if (capatchaInventory.clickedCorrectItemHandler(event.getSlot(), (Player)event.getWhoClicked()) == 0) {
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

        if(!isIsEnabled()) return false;

        //Check if player still needs to do capatcha
        if (!hasCapatcha(player.getUniqueId())) return false;

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaInventory)) {
            //Re-open menu
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to interact with the inventory
     */
    public static boolean onInventoryInteractHandler(Inventory inventory, Player player) {

        if(!isIsEnabled()) return false;

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check inventory is a capatcha Inventory
        if (!inventory.equals(capatchaInventory.getInventory())) return false;

        return true;
    }

    /*
    When a player tries to interact
     */
    public static boolean onPlayerInteractHandler(Player player) {

        if(!isIsEnabled()) return false;

        //Check if player still needs to do capatcha
        if (!hasCapatcha(player.getUniqueId())) return false;

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaInventory)) {
            //Re-open menu
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to move
     */
    public static boolean onPlayerMoveHandler(Player player) {

        if(!isIsEnabled()) return false;

        //Check if player still needs to do capatcha
        if (!hasCapatcha(player.getUniqueId())) return false;

        CapatchaInventory capatchaInventory = getCapatchaMenu(player.getUniqueId());

        //Check if menu is open
        if (!player.getOpenInventory().equals(capatchaInventory)) {
            //Re-open menu
            openCapatchaMenu(player);
        }

        return true;
    }

    /*
    Get menu reference for player
     */
    private static CapatchaInventory getCapatchaMenu(UUID uuid) {
        if (openCapatchaMenus.containsKey(uuid)) {
            System.out.println("get");
            //Update current inventory
            return openCapatchaMenus.get(uuid);
        } else {
            System.out.println("generate");
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

        openCapatchaMenus.remove(uuid);
    }

}
