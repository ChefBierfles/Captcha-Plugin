package nl.chefbierfles.captcha.module.base;

public abstract class BaseModule {

    protected boolean isEnabled;

    protected static boolean isIsEnabled() {
        //TODO: Hook up to config
        return true;
    }
}
