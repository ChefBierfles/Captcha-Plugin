package nl.chefbierfles.captcha.interfaces;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public interface IDatabaseModule {

    /**
     * Handle connection to database
     * @param connectionString
     */
    void connect(String connectionString);

    /**
     * Add CaptchaData
     * @param uuid
     * @param date
     */
    void addCaptchaData(UUID uuid, Date date);

    /**
     * Update CaptchaData
     * @param uuid
     * @param date
     */
    void updateCaptchaData(UUID uuid, Date date);

    /**
     * Get CaptchaData
     * @param player
     * @return
     */
    Date getCaptchaData(Player player);
}
