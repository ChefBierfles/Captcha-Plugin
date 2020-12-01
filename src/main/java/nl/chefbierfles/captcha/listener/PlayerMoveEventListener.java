package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEventListener extends BaseListener {

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (getModuleManager().getCaptchaModule().onPlayerMoveHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
