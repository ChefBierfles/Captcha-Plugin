package nl.chefbierfles.captcha;

import nl.chefbierfles.captcha.listener.*;
import nl.chefbierfles.captcha.managers.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    public ModuleManager moduleManager = new ModuleManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new InventoryClickEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(), this);

        if (!databaseModule.connect(
                "admin",
                "O5oHINE77BvE",
                "cluster0.zfbz8.mongodb.net", "Capatcha")) {
            //Don't launch plugin if database connection doesnt succeeed
            onDisable();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
