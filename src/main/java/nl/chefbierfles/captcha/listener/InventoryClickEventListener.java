package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventListener extends BaseListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (getModuleManager().getCaptchaModule().onInventoryClickHandler(event)) {
            event.setCancelled(true);
        }
    }
}
