package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener extends BaseListener {

    public PlayerMoveEventListener(CaptchaModule captchaModule) {
        super(captchaModule);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (getCaptchaModule().onPlayerMoveHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
