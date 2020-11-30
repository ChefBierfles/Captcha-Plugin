package nl.chefbierfles.captcha.events;

import nl.chefbierfles.captcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public static void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        CapatchaModule.onPlayerQuitHandler(event.getPlayer());
    }
}
