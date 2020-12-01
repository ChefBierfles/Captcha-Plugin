package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEventListener extends BaseListener {

    @EventHandler
    public void onPlayerJoinEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        moduleManager.getCaptchaModule().onPlayerJoinHandler(event.getPlayer());
    }

}
