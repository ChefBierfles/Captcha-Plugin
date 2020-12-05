package nl.chefbierfles.captcha.interfaces;

public interface ConfigManager {

    /**
     * Load config file into memory
     */
    void loadConfig();

    /**
     * Get connectionString config value from memory
     * @return
     */
    String getDatabaseConnectionString();
}
