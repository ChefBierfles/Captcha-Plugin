package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerInteractEventListener extends BaseListener {

    public PlayerInteractEventListener(CaptchaModule captchaModule) {
        super(captchaModule);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (getCaptchaModule().onPlayerInteractHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
