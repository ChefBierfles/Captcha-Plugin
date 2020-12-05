package nl.chefbierfles.captcha.managers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.interfaces.IConfigManager;
import nl.chefbierfles.captcha.module.CaptchaModule;
import nl.chefbierfles.captcha.module.base.BaseModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
