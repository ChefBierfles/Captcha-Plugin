package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEventListener extends BaseListener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        moduleManager.getCaptchaModule().onPlayerQuitHandler(event.getPlayer());
    }
}