package de.tsgscraft.paperPresets.ClickableInventory.Items;

import de.tsgscraft.paperPresets.ClickableInventory.ClickableInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ChangeItem extends ItemStack {

    private final Map<String, ChangeItemVariant> variants;

    private UUID uuid;

    private final ChangeItemBuilder builder;

    private @Nullable ChangeItemVariant selected;

    public ChangeItem(ChangeItemBuilder builder){
        super(builder.getFallback());
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
    }
}
