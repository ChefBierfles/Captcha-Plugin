package nl.chefbierfles.captcha.interfaces;

import nl.chefbierfles.captcha.module.CaptchaModule;
import nl.chefbierfles.captcha.module.DatabaseModule;
import nl.chefbierfles.captcha.module.base.BaseModule;

public interface IModuleManager {

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
