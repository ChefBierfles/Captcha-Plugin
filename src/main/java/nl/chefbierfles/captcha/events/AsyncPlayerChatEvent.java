package nl.chefbierfles.captcha.events;

import nl.chefbierfles.captcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatEvent implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        if (CapatchaModule.onAsyncPlayerChatHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
