package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener extends BaseListener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        getModuleManager().getCaptchaModule().onPlayerQuitHandler(event.getPlayer());
    }
}