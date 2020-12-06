package nl.chefbierfles.captcha.interfaces;

import nl.chefbierfles.captcha.models.menus.CaptchaMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface ICaptchaManager {

    /**
     * Get CaptchaMenu for player
     * @param player
     * @return
     */
    CaptchaMenu getCaptchaMenu(Player player);

    /**
     * Update CaptchaMenu contents
     * @param player
     * @param captchaMenu
     */
    void updateCaptchaMenu(Player player, CaptchaMenu captchaMenu);

    /**
     * Check if player has active Captcha
     * @param uuid
     * @return
     */
    boolean hasCaptcha(UUID uuid);

    /**
     * Remove CaptchaMenu reference
     * @param uuid
     */
    void removeCaptcha(UUID uuid);

    /**
     * Finish CaptchaMenu
     * @param uuid
     */
    void finishCaptcha(UUID uuid);

    /**
     * Open CaptchaMenu
     * @param player
     */
    void openCaptchaMenu(Player player);
}
