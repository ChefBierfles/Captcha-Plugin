package nl.chefbierfles.capatcha.events;

import nl.chefbierfles.capatcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public static void onPlayerJoinEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        CapatchaModule.onPlayerJoinHandler(event.getPlayer());

    }

}
