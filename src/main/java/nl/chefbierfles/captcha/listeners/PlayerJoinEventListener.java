package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener extends BaseListener {

    public PlayerJoinEventListener(CaptchaModule captchaModule) {
        super(captchaModule);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        getCaptchaModule().onPlayerJoinHandler(event.getPlayer());
    }

}
