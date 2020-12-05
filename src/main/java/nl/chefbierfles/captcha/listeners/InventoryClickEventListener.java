package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventListener extends BaseListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (getModuleManager().getCaptchaModule().onInventoryClickHandler(event)) {
            event.setCancelled(true);
        }
    }
}
