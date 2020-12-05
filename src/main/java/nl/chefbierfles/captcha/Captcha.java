package nl.chefbierfles.captcha;

import nl.chefbierfles.captcha.listeners.*;
import nl.chefbierfles.captcha.managers.ConfigManager;
import nl.chefbierfles.captcha.managers.ModuleManager;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import nl.chefbierfles.captcha.modules.DatabaseModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class Captcha extends JavaPlugin {

    public ModuleManager moduleManager;
    public ConfigManager configManager;

    @Override
    public void onEnable() {

        configManager = new ConfigManager(this);
        moduleManager = new ModuleManager();
        moduleManager.registerModule(new DatabaseModule());
        moduleManager.registerModule(new CaptchaModule(moduleManager.getDatabaseModule()));

        getServer().getPluginManager().registerEvents(new InventoryClickEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(), this);

    }

    @Override
    public void onDisable() { }
}
