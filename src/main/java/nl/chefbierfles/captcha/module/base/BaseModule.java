package nl.chefbierfles.captcha.module.base;

import nl.chefbierfles.captcha.Plugin;
import nl.chefbierfles.captcha.managers.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseModule {

    protected String name;
    protected boolean isEnabled;
    private ModuleManager moduleManager = JavaPlugin.getPlugin(Plugin.class).moduleManager;

    protected boolean isEnabled() {
        //TODO: Hook up to config
        return isEnabled;
    }

    protected void toggleEnabled() {
        isEnabled = !isEnabled;
    }

    protected void toggleEnabled(boolean value) {
        isEnabled = value;
    }

    public String getName() {
        return name;
    }

    protected ModuleManager getModuleManager() {
        return moduleManager;
    }
}
