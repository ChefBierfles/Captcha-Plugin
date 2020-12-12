package nl.chefbierfles.captcha.listeners.base;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.modules.CaptchaModule;
import nl.chefbierfles.captcha.modules.DatabaseModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseListener implements Listener {

    protected Captcha pluginInstance = (Captcha) JavaPlugin.getProvidingPlugin(Captcha.class);

    protected DatabaseModule getDatabaseModule() {
        return pluginInstance.databaseModule;
    }

    protected CaptchaModule getCaptchaModule() {
        return pluginInstance.captchaModule;
    }

}
