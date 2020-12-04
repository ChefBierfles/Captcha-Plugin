package nl.chefbierfles.captcha.listener.base;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.managers.ModuleManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseListener implements Listener {

    private ModuleManager moduleManager = JavaPlugin.getPlugin(Captcha.class).moduleManager;

    protected ModuleManager getModuleManager() {
        return moduleManager;
    }
}
