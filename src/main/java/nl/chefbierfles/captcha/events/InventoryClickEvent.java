package nl.chefbierfles.captcha.events;

import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (CaptchaModule.onInventoryClickHandler(event)) {
            event.setCancelled(true);
        }
    }
}
