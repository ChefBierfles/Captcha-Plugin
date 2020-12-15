package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickEventListener extends BaseListener {

    public InventoryClickEventListener(CaptchaModule captchaModule) {
        super(captchaModule);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (getCaptchaModule().onInventoryClickHandler(event)) {
            event.setCancelled(true);
        }
    }
}
