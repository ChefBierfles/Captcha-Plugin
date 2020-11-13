package nl.chefbierfles.capatcha;

import nl.chefbierfles.capatcha.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveEvent(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
