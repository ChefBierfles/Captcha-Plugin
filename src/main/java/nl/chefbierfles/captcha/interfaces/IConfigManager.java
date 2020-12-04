package nl.chefbierfles.captcha.interfaces;

public interface IConfigManager {

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
