package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventListener extends BaseListener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (getModuleManager().getCaptchaModule().onPlayerInteractHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
