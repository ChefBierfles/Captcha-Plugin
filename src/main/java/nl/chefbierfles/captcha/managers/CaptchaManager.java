package nl.chefbierfles.captcha.managers;

import nl.chefbierfles.captcha.models.menus.CaptchaMenu;
import nl.chefbierfles.captcha.module.DatabaseModule;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public final class CaptchaManager {

    private HashMap<UUID, CaptchaMenu> OPEN_CAPTCHA_MENUS = new HashMap<>();
    private DatabaseModule databaseModule;

    public CaptchaManager(DatabaseModule databaseModule) {
        this.databaseModule = databaseModule;
    }

    /*
    Get menu reference for player
    */
    public CaptchaMenu getCaptchaMenu(Player player) {
        if (OPEN_CAPTCHA_MENUS.containsKey(player.getUniqueId())) {
            //Update current inventory
            return OPEN_CAPTCHA_MENUS.get(player.getUniqueId());
        } else {
            CaptchaMenu captchaMenu = new CaptchaMenu(player);
            OPEN_CAPTCHA_MENUS.put(player.getUniqueId(), captchaMenu);
            return captchaMenu;
        }
    }

    /*
    Update menu contents
     */
    public void updateCaptchaMenu(Player player, CaptchaMenu captchaMenu) {
        captchaMenu.updateMenu(player);
        OPEN_CAPTCHA_MENUS.put(player.getUniqueId(), captchaMenu);
    }

    /*
    Check if player needs to do the captcha
     */
    public boolean hasCaptcha(UUID uuid) {
        return OPEN_CAPTCHA_MENUS.containsKey(uuid);
    }

    /*
    Remove menu reference for player
     */
    public void removeCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }

    /*
    Remove menu reference for player
    */
    public void finishCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        //Zet database waarde om over een maand weer te controleren
        databaseModule.addCapatchaData(uuid, Calendar.getInstance().getTime());

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }

    /*
    Open inventory
    */
    public void openCaptchaMenu(Player player) {
        CaptchaMenu captchaMenu = getCaptchaMenu(player);
        player.openInventory(captchaMenu.getInventory());
    }
}
