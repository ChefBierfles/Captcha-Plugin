package nl.chefbierfles.captcha.listener;

import nl.chefbierfles.captcha.Plugin;
import nl.chefbierfles.captcha.listener.base.BaseListener;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AsyncPlayerChatEventListener extends BaseListener {

    @EventHandler
    public void onAsyncPlayerChatEvent(org.bukkit.event.player.AsyncPlayerChatEvent event) {

        if (moduleManager.getCaptchaModule().onAsyncPlayerChatHandler(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
