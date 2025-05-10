package de.tsgscraft.paperPresets.ClickableInventory.Items;

import de.tsgscraft.paperPresets.ClickableInventory.ClickableInventory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class InventoryChangeItems {

    private final ClickableInventory clickableInventory;
    private List<Integer> slot = new ArrayList<>();
    private final ChangeItem item;

    public InventoryChangeItems(ClickableInventory clickableInventory, ChangeItem item){
        this.clickableInventory = clickableInventory;
        this.item = item;
        for (int i = 0; i < clickableInventory.getInventory().getSize(); i++){
            if (clickableInventory.getInventory().getItem(i) != null && !clickableInventory.getInventory().getItem(i).getType().equals(Material.AIR)) {
                ItemStack itemStack = clickableInventory.getInventory().getItem(i);
                if (itemStack.hasItemMeta()) {
                    NamespacedKey key = ClickableInventory.getChangeKey();
                    PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
                    if (container.has(key, PersistentDataType.STRING)) {
                        String value = container.get(key, PersistentDataType.STRING);
                        if (item.getUniqueId().toString().equals(value)) {
                            slot.add(i);
                        }
                    }
                }
            }
        }
    }

    public ChangeItem getItem() {
        return item;
    }

    public List<Integer> getSlot() {
        return slot;
    }

    public ClickableInventory getClickableInventory() {
        return clickableInventory;
    }
}
