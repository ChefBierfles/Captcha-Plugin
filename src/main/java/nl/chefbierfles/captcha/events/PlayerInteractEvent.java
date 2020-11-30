package nl.chefbierfles.captcha.events;

import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractEvent implements Listener {

    @EventHandler
    public static void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        if (CaptchaModule.onPlayerInteractHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
