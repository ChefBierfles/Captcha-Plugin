package nl.chefbierfles.captcha.modules.base;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.managers.ConfigManager;
import nl.chefbierfles.captcha.managers.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseModule {

    protected String name;
    private ModuleManager moduleManager;
    private ConfigManager configManager;

    public BaseModule() {
        moduleManager = JavaPlugin.getPlugin(Captcha.class).moduleManager;
        configManager = JavaPlugin.getPlugin(Captcha.class).configManager;
    }

    public String getName() {
        return name;
    }

    protected ModuleManager getModuleManager() {
        return moduleManager;
    }

    protected ConfigManager getConfigManager() {
        return configManager;
    }
}
