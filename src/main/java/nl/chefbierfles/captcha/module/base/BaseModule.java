package nl.chefbierfles.captcha.module.base;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.managers.ConfigManager;
import nl.chefbierfles.captcha.managers.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseModule {

    protected String name;
    protected boolean isEnabled;
    private ModuleManager moduleManager;
    private ConfigManager configManager;

    public BaseModule() {
        moduleManager = JavaPlugin.getPlugin(Captcha.class).moduleManager;
        configManager = JavaPlugin.getPlugin(Captcha.class).configManager;
    }

    protected boolean isEnabled() {
        //TODO: Hook up to config
        return isEnabled;
    }

    protected void toggleModule() {
        isEnabled = !isEnabled;
    }

    protected void enableModule(boolean value) {
        isEnabled = true;
    }

    protected void disableModule(boolean value) {
        isEnabled = false;
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
