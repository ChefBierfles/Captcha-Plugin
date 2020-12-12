package nl.chefbierfles.captcha.managers;

import nl.chefbierfles.captcha.interfaces.ICaptchaManager;
import nl.chefbierfles.captcha.models.menus.CaptchaMenu;
import nl.chefbierfles.captcha.modules.DatabaseModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public final class CaptchaManager implements ICaptchaManager {

    private HashMap<UUID, CaptchaMenu> OPEN_CAPTCHA_MENUS = new HashMap<>();
    private DatabaseModule databaseModule;

    public CaptchaManager(DatabaseModule databaseModule) {
        this.databaseModule = databaseModule;
    }

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

    public void updateCaptchaMenu(Player player, CaptchaMenu captchaMenu) {
        captchaMenu.updateMenu(player);
        OPEN_CAPTCHA_MENUS.put(player.getUniqueId(), captchaMenu);
    }

    public boolean hasCaptcha(UUID uuid) {
        return OPEN_CAPTCHA_MENUS.containsKey(uuid);
    }

    public void removeCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }

    public void finishCaptcha(UUID uuid) {
        if (!OPEN_CAPTCHA_MENUS.containsKey(uuid)) return;

        //Zet database waarde om over een maand weer te controleren
        databaseModule.addCaptchaData(uuid, Calendar.getInstance().getTime());

        OPEN_CAPTCHA_MENUS.remove(uuid);
    }

    public void openCaptchaMenu(Player player) {
        CaptchaMenu captchaMenu = getCaptchaMenu(player);
        player.openInventory(captchaMenu.getInventory());
    }
}
