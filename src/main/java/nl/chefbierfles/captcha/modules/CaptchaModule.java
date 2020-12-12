package nl.chefbierfles.captcha.modules;

import nl.chefbierfles.captcha.Captcha;
import nl.chefbierfles.captcha.helpers.SkullHelper;
import nl.chefbierfles.captcha.helpers.constants.Permissions;
import nl.chefbierfles.captcha.interfaces.ICaptchaModule;
import nl.chefbierfles.captcha.managers.CaptchaManager;
import nl.chefbierfles.captcha.models.menus.CaptchaMenu;
import org.apache.commons.lang3.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public final class CaptchaModule implements ICaptchaModule {

    private CaptchaManager captchaManager;
    private DatabaseModule databaseModule;

    public CaptchaModule(DatabaseModule databaseModule) {
        captchaManager = new CaptchaManager(databaseModule);
        this.databaseModule = databaseModule;
    }

    /*
    When player joins and should do a captcha
     */
    public void onPlayerJoinHandler(Player player) {

        if (player.hasPermission(Permissions.PERMISSION_CAPTCHA_BYPASS)) return;

        CompletableFuture.supplyAsync(() -> databaseModule.getCaptchaData(player))
                .thenAccept(lastDoneDate -> {
                    if (lastDoneDate == null || new Date().after(DateUtils.addMonths(lastDoneDate, 1))) {
                        captchaManager.openCaptchaMenu(player);
                    }
                });
    }

    /*
    When player quits
     */
    public void onPlayerQuitHandler(Player player) {
        captchaManager.removeCaptcha(player.getUniqueId());
    }

    /*
    When a player tries to click a inventory slot
     */
    public boolean onInventoryClickHandler(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return false;

        if (event.getClickedInventory() == null) return false;

        if (!captchaManager.hasCaptcha(event.getWhoClicked().getUniqueId())) return false;

        CaptchaMenu captchaMenu = captchaManager.getCaptchaMenu((Player) event.getWhoClicked());

        //Check inventory is a captcha Inventory
        if (event.getClickedInventory().getTitle() != captchaMenu.getInventory().getTitle()) return false;

        ItemStack clickedItem = event.getCurrentItem();

        //Check if capatchaItem is clicked
        if (SkullHelper.isSameSkull(captchaMenu.getInvalidItem(), clickedItem) || SkullHelper.isSameSkull(captchaMenu.getSecondInvalidItem(), clickedItem)) {

            //Check of maximaal bereikt is
            if (captchaMenu.getMistakesMade() >= captchaMenu.getMaxMistakes()) {
                ((Player) event.getWhoClicked()).kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                captchaManager.removeCaptcha(event.getWhoClicked().getUniqueId());
                return true;
            }

            captchaMenu.addMistake();
            captchaManager.updateCaptchaMenu((Player) event.getWhoClicked(), captchaMenu);
        }

        if (SkullHelper.isSameSkull(captchaMenu.getCorrectItem(), clickedItem)) {

            if (captchaMenu.replaceCorrectItem(event.getSlot(), (Player) event.getWhoClicked()) == 0) {
                //Captcha is done
                captchaManager.finishCaptcha(event.getWhoClicked().getUniqueId());

                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Je hebt de captcha succesvol afgerond.");
            }
        }

        return true;
    }

    /*
    When a player tries to speak
     */
    public boolean onAsyncPlayerChatHandler(Player player) {

        //Check if player still needs to do captcha
        if (!captchaManager.hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = captchaManager.getCaptchaMenu(player);

        //Check if menu is open
        if (player.getOpenInventory().getTopInventory().getTitle() != captchaMenu.getInventory().getTitle()) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() >= captchaMenu.getMaxInventoryClosed()) {
                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(Captcha.class), () -> {
                    player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                });
                return true;
            }
            captchaMenu.addInventoryClosed();
            captchaManager.openCaptchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to interact
     */
    public boolean onPlayerInteractHandler(Player player) {

        //Check if player still needs to do captcha
        if (!captchaManager.hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = captchaManager.getCaptchaMenu(player);

        //Check if menu is open
        if (player.getOpenInventory().getTopInventory().getTitle() != captchaMenu.getInventory().getTitle()) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() >= captchaMenu.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            captchaMenu.addInventoryClosed();
            captchaManager.openCaptchaMenu(player);
        }

        return true;
    }

    /*
    When a player tries to move
     */
    public boolean onPlayerMoveHandler(Player player) {

        //Check if player still needs to do captcha
        if (!captchaManager.hasCaptcha(player.getUniqueId())) return false;

        CaptchaMenu captchaMenu = captchaManager.getCaptchaMenu(player);

        //Check if menu is open
        if (player.getOpenInventory().getTopInventory().getTitle() != captchaMenu.getInventory().getTitle()) {
            //Re-open menu
            if (captchaMenu.getInventoryClosed() >= captchaMenu.getMaxInventoryClosed()) {
                player.kickPlayer(ChatColor.RED + "Te veel ongeldige pogingen!");
                return true;
            }
            captchaMenu.addInventoryClosed();
            captchaManager.openCaptchaMenu(player);
        }

        return true;
    }

}
