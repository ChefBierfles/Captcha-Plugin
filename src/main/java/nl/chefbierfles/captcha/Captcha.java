package nl.chefbierfles.captcha;

import nl.chefbierfles.captcha.listeners.*;
import nl.chefbierfles.captcha.managers.ConfigManager;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import nl.chefbierfles.captcha.modules.DatabaseModule;
import org.bukkit.plugin.java.JavaPlugin;

public final class Captcha extends JavaPlugin {

    public ConfigManager configManager;
    public CaptchaModule captchaModule;
    public DatabaseModule databaseModule;

    @Override
    public void onEnable() {

        configManager = new ConfigManager(this);
        databaseModule = new DatabaseModule(configManager);
        captchaModule = new CaptchaModule(databaseModule);

        getServer().getPluginManager().registerEvents(new InventoryClickEventListener(captchaModule), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(captchaModule), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(captchaModule), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(captchaModule), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventListener(captchaModule), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(captchaModule), this);

    }

    @Override
    public void onDisable() {
    }
}
