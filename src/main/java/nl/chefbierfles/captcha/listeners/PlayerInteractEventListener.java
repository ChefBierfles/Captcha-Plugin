package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventListener extends BaseListener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (getCaptchaModule().onPlayerInteractHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
