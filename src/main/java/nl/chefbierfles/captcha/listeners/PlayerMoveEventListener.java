package nl.chefbierfles.captcha.listeners;

import nl.chefbierfles.captcha.listeners.base.BaseListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener extends BaseListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (getCaptchaModule().onPlayerMoveHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
