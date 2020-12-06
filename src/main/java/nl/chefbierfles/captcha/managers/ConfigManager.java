package nl.chefbierfles.captcha.managers;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.interfaces.IConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ConfigManager implements IConfigManager {

    private Captcha captcha;

    private String username;
    private String password;
    private String hostName;
    private String dbName;
    private String port;

    public ConfigManager(JavaPlugin plugin) {
        captcha = (Captcha) plugin;

        loadConfig();
    }

    public void loadConfig() {
        captcha.saveDefaultConfig();

        username = captcha.getConfig().getString("modules.database.username");
        password = captcha.getConfig().getString("modules.database.password");
        dbName = captcha.getConfig().getString("modules.database.dbname");
        hostName = captcha.getConfig().getString("modules.database.hostname");
        port = captcha.getConfig().getString("modules.database.port");

        if (hostName.isEmpty()) {
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "Hostname moet een waarde bevatten in de config! Plugin word niet geladen");
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHostname() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return this.dbName;
    }
}
