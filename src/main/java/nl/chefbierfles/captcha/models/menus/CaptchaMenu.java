package nl.chefbierfles.captcha.models.menus;

import nl.chefbierfles.captcha.helpers.constants.captcha.CaptchaOptions;
import nl.chefbierfles.captcha.models.CaptchaItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Random;

public class CaptchaMenu {

    private Inventory inventory;
    private CaptchaItem correctItem;
    private CaptchaItem invalidItem;
    private CaptchaItem secondInvalidItem;

    private ItemStack backgroundItem;
    private ItemStack informationItem;

    private int mistakesMade = 0;
    private final int maxMistakes = 3;

    private int inventoryClosed = 0;
    private final int maxInventoryClosed = 3;

    public CaptchaMenu(InventoryHolder inventoryHolder) {

        initMenu(inventoryHolder);
    }

    public ItemStack getCorrectItem() {
        return correctItem.getItemStack();
    }

    public ItemStack getInvalidItem() {
        return invalidItem.getItemStack();
    }

    public ItemStack getSecondInvalidItem() {
        return secondInvalidItem.getItemStack();
    }

    public void updateMenu(Player player) {

        getRandomColorOption();
        inventory.setContents(generateRandomCapatchaSlots());
        player.updateInventory();

    }

    public Inventory initMenu(InventoryHolder inventoryHolder) {
        inventory = Bukkit.createInventory(inventoryHolder, 54, ChatColor.DARK_GRAY + "Capatcha");

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
        inventoryContents[slot] = Math.random() > 0.5 ? getInvalidItem() : getSecondInvalidItem();
        inventory.setContents(inventoryContents);

        int correctItemsLeft = 0;

        for (int index = 0; index < inventoryContents.length; index++) {

            if (inventoryContents[index].isSimilar(correctItem.getItemStack())) {
                correctItemsLeft++;
            }
        }

        player.updateInventory();

        return correctItemsLeft;
    }

    private void getRandomColorOption() {
        CaptchaItem[] captchaOptions = CaptchaOptions.getOptions();

        //Pick random first color
        correctItem = captchaOptions[new Random().nextInt(captchaOptions.length)];
        //Keep picking till correct item doesnt match invlid item
        while (invalidItem == null || correctItem == invalidItem) {
            invalidItem = captchaOptions[new Random().nextInt(captchaOptions.length)];
        }

        while (secondInvalidItem == null || correctItem == secondInvalidItem || secondInvalidItem == invalidItem) {
            secondInvalidItem = captchaOptions[new Random().nextInt(captchaOptions.length)];
        }

        SkullMeta correctItemMeta = (SkullMeta) correctItem.getItemStack().getItemMeta();
        correctItemMeta.setDisplayName(" ");
        correctItem.getItemStack().setItemMeta(correctItemMeta);

        SkullMeta invalidItemMeta = (SkullMeta) invalidItem.getItemStack().getItemMeta();
        invalidItemMeta.setDisplayName(" ");
        invalidItem.getItemStack().setItemMeta(invalidItemMeta);

        SkullMeta secondInvalidItemMeta = (SkullMeta) secondInvalidItem.getItemStack().getItemMeta();
        secondInvalidItemMeta.setDisplayName(" ");
        secondInvalidItem.getItemStack().setItemMeta(secondInvalidItemMeta);
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

                if (correctItemChance < 0.20) {
                    inventoryContents[index] = correctItem.getItemStack();
                } else {
                    ItemStack invalidItemStack = Math.random() > 0.5 ? invalidItem.getItemStack() : secondInvalidItem.getItemStack();
                    inventoryContents[index] = invalidItemStack;
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
