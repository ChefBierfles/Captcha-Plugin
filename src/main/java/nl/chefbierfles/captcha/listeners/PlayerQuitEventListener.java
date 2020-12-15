package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener extends BaseListener {

    public PlayerQuitEventListener(CaptchaModule captchaModule) {
        super(captchaModule);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        getCaptchaModule().onPlayerQuitHandler(event.getPlayer());
    }
}