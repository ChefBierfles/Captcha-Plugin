package nl.chefbierfles.captcha.helpers.constants.captcha;

import nl.chefbierfles.captcha.helpers.SkullHelper;
import nl.chefbierfles.captcha.helpers.constants.PlayerSkullTextures;
import nl.chefbierfles.captcha.models.CaptchaItem;
import org.bukkit.inventory.ItemStack;

public class CaptchaOptions {

    /*
    Could be hooked up to a config
     */
    private final static CaptchaItem[] options = new CaptchaItem[] {
            new CaptchaItem(SkullHelper.itemWithBase64(PlayerSkullTextures.COLOR_LIME), "groene"),
            new CaptchaItem(SkullHelper.itemWithBase64(PlayerSkullTextures.COLOR_RED), "rode"),
            new CaptchaItem(SkullHelper.itemWithBase64(PlayerSkullTextures.COLOR_ROYAL_BLUE), "blauwe"),
            new CaptchaItem(SkullHelper.itemWithBase64(PlayerSkullTextures.COLOR_WHITE), "witte"),
            new CaptchaItem(SkullHelper.itemWithBase64(PlayerSkullTextures.COLOR_HOT_PINK), "roze"),
            new CaptchaItem(SkullHelper.itemWithBase64(PlayerSkullTextures.COLOR_ORANGE), "oranje")
    };

    public static CaptchaItem[] getOptions() {
        return options;
    }
}
