package nl.chefbierfles.captcha.models;

import org.bukkit.inventory.ItemStack;

public class CapatchaItem {

    private ItemStack itemStack;
    private String description;

    public CapatchaItem(ItemStack itemStack, String description) {
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
