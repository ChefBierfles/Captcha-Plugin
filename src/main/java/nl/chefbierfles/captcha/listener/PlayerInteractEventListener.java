package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractEventListener extends BaseListener {

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        if (getModuleManager().getCaptchaModule().onPlayerInteractHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
