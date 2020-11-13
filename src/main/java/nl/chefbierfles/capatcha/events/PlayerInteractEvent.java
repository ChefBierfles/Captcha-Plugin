package nl.chefbierfles.capatcha.events;

import nl.chefbierfles.capatcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractEvent implements Listener {

    @EventHandler
    public static void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        if (CapatchaModule.onPlayerInteractHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
