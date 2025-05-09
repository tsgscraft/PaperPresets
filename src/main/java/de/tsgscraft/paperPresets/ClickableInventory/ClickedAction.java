package de.tsgscraft.paperPresets.ClickableInventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickedAction {
    void run(InventoryClickEvent event, ClickableInventory inv);
}
