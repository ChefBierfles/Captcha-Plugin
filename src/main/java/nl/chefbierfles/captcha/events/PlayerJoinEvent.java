package nl.chefbierfles.captcha.events;

import nl.chefbierfles.captcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public static void onPlayerJoinEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        CapatchaModule.onPlayerJoinHandler(event.getPlayer());

    }

}
