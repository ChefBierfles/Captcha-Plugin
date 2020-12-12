package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener extends BaseListener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        getCaptchaModule().onPlayerQuitHandler(event.getPlayer());
    }
}