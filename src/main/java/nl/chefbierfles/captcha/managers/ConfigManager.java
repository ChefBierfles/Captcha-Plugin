package nl.chefbierfles.captcha.managers;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.interfaces.IConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager implements IConfigManager {

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
