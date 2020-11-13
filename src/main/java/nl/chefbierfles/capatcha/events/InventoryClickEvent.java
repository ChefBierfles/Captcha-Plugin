package nl.chefbierfles.capatcha.events;

import nl.chefbierfles.capatcha.module.CapatchaModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (CapatchaModule.onInventoryClickHandler(event)) {
            event.setCancelled(true);
        }
    }
}
