package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEventListener extends BaseListener {

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (moduleManager.getCaptchaModule().onInventoryClickHandler(event)) {
            event.setCancelled(true);
        }
    }
}
