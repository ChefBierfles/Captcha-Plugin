package nl.chefbierfles.captcha.events;

import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (CaptchaModule.onPlayerMoveHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
