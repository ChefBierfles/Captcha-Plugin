package nl.chefbierfles.capatcha.events;

import nl.chefbierfles.capatcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (CapatchaModule.onPlayerMoveHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
