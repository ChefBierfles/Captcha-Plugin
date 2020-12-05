package nl.chefbierfles.captcha.managers;

import nl.chefbierfles.captcha.Captcha;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager implements nl.chefbierfles.captcha.interfaces.ConfigManager {

    private Captcha captcha;

    private String databaseConnectionString;

    public ConfigManager(JavaPlugin plugin) {
        captcha = (Captcha) plugin;

        loadConfig();
    }

    public void loadConfig() {
        captcha.saveDefaultConfig();
    }

    public String getDatabaseConnectionString() {
        return databaseConnectionString;
    }
}
