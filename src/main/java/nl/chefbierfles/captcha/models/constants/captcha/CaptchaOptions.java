package nl.chefbierfles.captcha.models.constants.captcha;

import nl.chefbierfles.captcha.helpers.SkullHelper;
import nl.chefbierfles.captcha.models.CapatchaItem;
import nl.chefbierfles.captcha.models.constants.PlayerHeads;

public class CaptchaOptions {

    /*
    Could be hooked up to a config
     */
    private final static CapatchaItem[] options = new CapatchaItem[]{
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_LIME), "groene"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_RED), "rode"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_ROYAL_BLUE), "blauwe"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_WHITE), "witte"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_DEEP_PINK), "roze"),
            new CapatchaItem(SkullHelper.itemWithBase64(PlayerHeads.COLOR_ORANGE), "oranje")
    };

    public static CapatchaItem[] getOptions() {
        return options;
    }
}
