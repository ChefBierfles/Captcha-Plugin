package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener extends BaseListener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        getModuleManager().getCaptchaModule().onPlayerJoinHandler(event.getPlayer());
    }

}
