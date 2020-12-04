package nl.chefbierfles.captcha.managers;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.module.CaptchaModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {

    private String databaseConnectionString;
    private Captcha captcha;

    public ConfigManager() {
        captcha = JavaPlugin.getPlugin(Captcha.class);
        loadConfig();
    }

    public void loadConfig() {
        captcha.saveDefaultConfig();
        databaseConnectionString = JavaPlugin.getPlugin(Captcha.class).getConfig().getString("modules.database.connection_string");
    }

    public String getDatabaseConnectionString() {
        return databaseConnectionString;
    }
}
