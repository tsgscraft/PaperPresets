package de.tsgscraft.paperPresets.ClickableInventory;

import de.tsgscraft.paperPresets.PaperPresets;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class ClickedListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory != null && clickedInventory.getHolder() instanceof ClickableInventory inv) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                inv.runAction(event.getCurrentItem(), event);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof ClickableInventory inv) {
            if (inv.hasPreviousInv()) {
                ClickableInventory pre = inv.getPreviousInv();
                PaperPresets.getScheduler().runTask(PaperPresets.getInstance(), () -> {
                    pre.openInventory();
                });
            }
        }
    }
}
