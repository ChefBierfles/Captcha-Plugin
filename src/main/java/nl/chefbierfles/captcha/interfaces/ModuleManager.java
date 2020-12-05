package nl.chefbierfles.captcha.interfaces;

import nl.chefbierfles.captcha.modules.CaptchaModule;
import nl.chefbierfles.captcha.modules.DatabaseModule;
import nl.chefbierfles.captcha.modules.base.BaseModule;

public interface ModuleManager {

    /**
     * Register a module
     * @param baseModule
     */
    void registerModule(BaseModule baseModule);

    /**
     * Get a module
     * @param moduleName
     * @return
     */
    BaseModule getModule(String moduleName);

    /**
     * Get all modules
     * @return
     */
    BaseModule[] getModules();

    /**
     * Get CaptchaModule
     * @return
     */
    CaptchaModule getCaptchaModule();

    /**
     * Get DatabaseModule
     * @return
     */
    DatabaseModule getDatabaseModule();
}
