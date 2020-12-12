package nl.chefbierfles.captcha.interfaces;

import nl.chefbierfles.captcha.modules.DatabaseModule;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ICaptchaModule {

    /**
     * Handle if player needs to do the Captcha when joining
     * @param player
     */
    void onPlayerJoinHandler(Player player);

    /**
     * Handle stored CaptchaMenu when player quits.
     * @param player
     */
    void onPlayerQuitHandler(Player player);

    /**
     * Handle Captcha inventory when clicking on it
     * @param event
     * @return If events needs to be cancelled
     */
    boolean onInventoryClickHandler(InventoryClickEvent event);

    /**
     * Handle ability to chat when Captcha is active
     * @param player
     * @return If events needs to be cancelled
     */
    boolean onAsyncPlayerChatHandler(Player player);

    /**
     * Handle ability to interact when Captcha is active
     * @param player
     * @return If events needs to be cancelled
     */
    boolean onPlayerInteractHandler(Player player);

    /**
     * Handle ability to move when Captcha is active
     * @param player
     * @return If events needs to be cancelled
     */
    boolean onPlayerMoveHandler(Player player);


}
