package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener extends BaseListener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        getModuleManager().getCaptchaModule().onPlayerJoinHandler(event.getPlayer());
    }

}
