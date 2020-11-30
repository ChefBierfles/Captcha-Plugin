package nl.chefbierfles.capatcha.models.menus;

import nl.chefbierfles.capatcha.helpers.SkullHelper;
import nl.chefbierfles.capatcha.models.CapatchaItem;
import nl.chefbierfles.capatcha.models.constants.PlayerHeads;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class CapatchaInventory {

    private Inventory inventory;
    private CapatchaItem correctItem;
    private CapatchaItem invalidItem;

    private ItemStack backgroundItem;
    private ItemStack informationItem;

    private int mistakesMade = 0;
    //TODO: Hookup to configfile
    private int maxMistakes = 3;

    private int inventoryClosed = 0;
    private int maxInventoryClosed = 3;

    /*
        Could be hooked up to a config
         */
    private CapatchaItem[] capatchaOptions = new CapatchaItem[]{
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_LIME.toString()), "groene"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_RED.toString()), "rode"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_ROYAL_BLUE.toString()), "blauwe"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_WHITE.toString()), "witte"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_DEEP_PINK.toString()), "roze"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_ORANGE.toString()), "oranje")
    };

    public CapatchaInventory() {

        initMenu();
    }

    public ItemStack getCorrectItem() {
        return correctItem.getItemStack();
    }

    public ItemStack getInvalidItem() {
        return invalidItem.getItemStack();
    }

    public void updateMenu(Player player) {

        getRandomColorOption();
        inventory.setContents(generateRandomCapatchaSlots());
        player.updateInventory();

    }

    public Inventory initMenu() {
        inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "Capatcha");

        getRandomColorOption();
        generateMenuBackground();

        //get generated capatcha
        inventory.setContents(generateRandomCapatchaSlots());

        return inventory;
    }

    public void addMistake(int increment) {
        this.mistakesMade += increment;
    }

    public void addMistake() {
        this.mistakesMade += 1;
    }

    public int getMistakesMade() {
        return mistakesMade;
    }

    public int getMaxMistakes() {
        return maxMistakes;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getInventoryClosed() {
        return inventoryClosed;
    }

    public int getMaxInventoryClosed() {
        return maxInventoryClosed;
    }

    public void addInventoryClosed() {
        this.inventoryClosed += 1;
    }

    /*
        When correct item has been clicked
         */
    public int replaceCorrectItem(int slot, Player player) {
        ItemStack[] inventoryContents = inventory.getContents();
        inventoryContents[slot] = getInvalidItem();
        inventory.setContents(inventoryContents);

        int correctItemsLeft = 0;

        for (int index = 0; index < inventoryContents.length; index++) {

            if (inventoryContents[index].getItemMeta().getDisplayName() == correctItem.getItemStack().getItemMeta().getDisplayName()) {
                correctItemsLeft++;
            }
        }

        player.updateInventory();

        return correctItemsLeft;
    }

    private void getRandomColorOption() {
        //Pick random first color
        correctItem = capatchaOptions[new Random().nextInt(capatchaOptions.length)];
        //Keep picking till correct item doesnt match invlid item
        while (invalidItem == null || correctItem == invalidItem) {
            invalidItem = capatchaOptions[new Random().nextInt(capatchaOptions.length)];
        }

        SkullMeta correctItemMeta = (SkullMeta) correctItem.getItemStack().getItemMeta();
        String correctItemDesc = correctItem.getDescription();
        correctItemMeta.setDisplayName(correctItemDesc.substring(0, 1).toUpperCase() + correctItemDesc.substring(1));
        correctItem.getItemStack().setItemMeta(correctItemMeta);

        SkullMeta invalidItemMeta = (SkullMeta) invalidItem.getItemStack().getItemMeta();
        String invalidItemDesc = invalidItem.getDescription();
        invalidItemMeta.setDisplayName(invalidItemDesc.substring(0, 1).toUpperCase() + invalidItemDesc.substring(1));
        invalidItem.getItemStack().setItemMeta(invalidItemMeta);
    }

    private void generateMenuBackground() {
        //Generate items;
        //region Glass item
        ItemStack glassItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta glassItemMeta = glassItem.getItemMeta();

        glassItemMeta.setDisplayName(" ");
        glassItem.setItemMeta(glassItemMeta);
        backgroundItem = glassItem;
        //endregion
    }

    private ItemStack[] generateRandomCapatchaSlots() {

        ItemStack[] inventoryContents = new ItemStack[inventory.getSize()];

        //region Sign item
        ItemStack signItem = new ItemStack(Material.SIGN);
        ItemMeta signItemMeta = signItem.getItemMeta();

        signItemMeta.setDisplayName(" ");
        signItemMeta.setLore(Arrays.asList(ChatColor.GOLD + "Klik alle " + correctItem.getDescription() + " blokjes aan", ""));
        signItem.setItemMeta(signItemMeta);
        informationItem = signItem;
        //endregion

        for (int index = 0; index < inventory.getSize(); index++) {

            if (index <= 8) {

                if (index == 4) {
                    inventoryContents[index] = informationItem;
                    continue;
                }

                inventoryContents[index] = backgroundItem;

                continue;
            }

            if (index > 8 && index <= 44) {

                double correctItemChance = Math.random();

                if (correctItemChance < 0.25) {
                    inventoryContents[index] = correctItem.getItemStack();
                } else {
                    inventoryContents[index] = invalidItem.getItemStack();
                }

                continue;
            }

            if (index >= 45) {
                inventoryContents[index] = backgroundItem;
            }

        }

        return inventoryContents;
    }
}
