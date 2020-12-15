package nl.chefbierfles.captcha.listeners.base;

import nl.chefbierfles.captcha.modules.CaptchaModule;
import org.bukkit.event.Listener;

public abstract class BaseListener implements Listener {

    private final CaptchaModule captchaModule;

    public BaseListener(CaptchaModule captchaModule) {
        this.captchaModule = captchaModule;
    }

    protected CaptchaModule getCaptchaModule() {
        return captchaModule;
    }
}
