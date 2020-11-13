package nl.chefbierfles.capatcha.events;

import nl.chefbierfles.capatcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public static void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        CapatchaModule.onPlayerQuitHandler(event.getPlayer());
    }
}
