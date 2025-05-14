package de.tsgscraft.paperPresets.ClickableInventory.Items;

import de.tsgscraft.paperPresets.ClickableInventory.ClickableInventory;
import de.tsgscraft.paperPresets.PaperPresets;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class ChangeItem extends ItemStack {

    private Plugin plugin;

    private final Map<String, ChangeItemVariant> variants;

    private UUID uuid;

    private final ChangeItemBuilder builder;

    private @Nullable ChangeItemVariant selected;

    public ChangeItem(ChangeItemBuilder builder, Plugin plugin){
        super(builder.getFallback());
        this.plugin = plugin;
        this.builder = builder;
        uuid = UUID.randomUUID();
        variants = builder.getVariants();
        this.editMeta(meta -> {
            meta.getPersistentDataContainer().set(ClickableInventory.getChangeKey(), PersistentDataType.STRING, uuid.toString());
        });
        variants.forEach((s, changeItemVariant) -> {
            changeItemVariant.editMeta(meta -> {
                meta.getPersistentDataContainer().set(ClickableInventory.getChangeKey(), PersistentDataType.STRING, uuid.toString());
            });
        });
        PaperPresets.debugLog(plugin, "Creating Changeable Item: " + builder.getName());
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public Map<String, ChangeItemVariant> getVariants() {
        return variants;
    }

    public void setActive(String variantID) {
        selected = variants.get(variantID);
    }

    public @Nullable ChangeItemVariant getSelected() {
        return selected;
    }

    public ChangeItemBuilder getBuilder() {
        return builder;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        this.editMeta(meta -> {
            meta.getPersistentDataContainer().set(ClickableInventory.getChangeKey(), PersistentDataType.STRING, uuid.toString());
        });
        variants.forEach((s, changeItemVariant) -> {
            changeItemVariant.editMeta(meta -> {
                meta.getPersistentDataContainer().set(ClickableInventory.getChangeKey(), PersistentDataType.STRING, uuid.toString());
            });
        });
    }
}
