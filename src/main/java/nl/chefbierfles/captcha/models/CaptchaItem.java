package nl.chefbierfles.captcha.models;

import org.bukkit.inventory.ItemStack;

public class CaptchaItem {

    private ItemStack itemStack;
    private String description;

    public CaptchaItem(ItemStack itemStack, String description) {
        this.itemStack = itemStack;
        this.description = description;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getDescription() {
        return description;
    }
}
