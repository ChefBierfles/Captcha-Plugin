package nl.chefbierfles.captcha.interfaces;

public interface IConfigManager {

    /**
     * Load the config file values to memory.
     */
    void loadConfig();
    /**
     * Get database username reference from memory
     * @return
     */
    String getUsername();
    /**
     * Get database password reference from memory
     * @return
     */
    String getPassword();
    /**
     * Get database hostname reference from memory
     * @return
     */
    String getHostname();
    /**
     * Get database port reference from memory
     * @return
     */
    String getPort();
    /**
     * Get database name reference from memory
     * @return
     */
    String getDbName();
}
