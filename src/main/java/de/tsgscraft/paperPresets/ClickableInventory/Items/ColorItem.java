package de.tsgscraft.paperPresets.ClickableInventory.Items;

import de.tsgscraft.paperPresets.ClickableInventory.ClickableInventory;
import de.tsgscraft.paperPresets.ClickableInventory.InventorySize;
import de.tsgscraft.paperPresets.ClickableInventory.ItemPos;
import de.tsgscraft.paperPresets.PaperPresets;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ColorItem {

    private Plugin plugin;

    private ChangeItem changeItem;

    private static final UUID baseColorUUID = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef");

    private static final NamespacedKey colorId = new NamespacedKey(PaperPresets.getInstance(), "selected_inv_color");

    private final Color defaultColor = Color.WHITE;

    public ColorItem(Player player, Plugin plugin) {
        this.plugin = plugin;
        Map<String, ChangeItemVariant> variants = new HashMap<>();
        for (Color color : Color.values()){
            variants.put(color.name(), new ChangeItemVariant(color.getMaterial(), color.name()).setName(Component.text(" ")).setHideTooltip(true));
        }
        changeItem = new ChangeItemBuilder(defaultColor.getMaterial(), plugin)
                .setName(Component.text(" "))
                .setVariants(variants)
                .build();
        changeItem.setUuid(baseColorUUID);
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (container.has(colorId, PersistentDataType.STRING)){
            String value = container.get(colorId, PersistentDataType.STRING);
            changeItem.setActive(value);
        }else {
            container.set(colorId, PersistentDataType.STRING, defaultColor.name());
            changeItem.setActive(defaultColor.name());
        }
    }

    public static void setVariant(Player player){
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (container.has(colorId, PersistentDataType.STRING)){
            String value = container.get(colorId, PersistentDataType.STRING);
            ClickableInventory.updateChangeItem(baseColorUUID, value);
        }
    }

    public static void setVariant(Player player, Color color){
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(colorId, PersistentDataType.STRING, color.name());
        setVariant(player);
    }

    public static ClickableInventory colorPicker(Plugin plugin, Player player, @Nullable Component name, @Nullable ClickableInventory pre){
        ColorItem colorItem = new ColorItem(player, plugin);

        ClickableInventory inv = new ClickableInventory(PaperPresets.getInstance(), InventorySize.SIX, Objects.requireNonNullElseGet(name, () -> Component.text("Color Picker")))
                .setItem(new ItemPos(0), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(1), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(2), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(3), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(4), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(6), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(7), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(8), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(0, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(1, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(2, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(3, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(4, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(5, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(6, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(7, 5), colorItem.getChangeItem(), null)
                .setItem(new ItemPos(8, 5), colorItem.getChangeItem(), null);

        for (Color color : Color.values()){
            inv.setItem(color.getPos(), color.getMaterial(), (event, inv1) -> ColorItem.setVariant((Player) event.getWhoClicked(), color));
        }

        if (pre == null) {
            inv.build(player);
        }else {
            inv.build(player, pre);
        }
        return inv;
    }

    public ChangeItem getChangeItem() {
        return changeItem;
    }

    public UUID getBaseColorUUID() {
        return baseColorUUID;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public enum Color{
        WHITE(new ItemPos(1, 2)),
        GRAY(new ItemPos(2, 2)),
        BLACK(new ItemPos(3, 2)),
        RED(new ItemPos(4, 2)),
        ORANGE(new ItemPos(5, 2)),
        YELLOW(new ItemPos(6, 2)),
        LIME(new ItemPos(7, 2)),
        GREEN(new ItemPos(1, 3)),
        CYAN(new ItemPos(2, 3)),
        LIGHT_BLUE(new ItemPos(3, 3)),
        BLUE(new ItemPos(4, 3)),
        PURPLE(new ItemPos(5, 3)),
        MAGENTA(new ItemPos(6, 3)),
        PINK(new ItemPos(7, 3));

        private final Material material;

        private final ItemPos pos;

        Color(ItemPos itemPos){
            material = Material.valueOf(name() + "_STAINED_GLASS_PANE");
            pos = itemPos;
        }

        public Material getMaterial() {
            return material;
        }

        public ItemPos getPos() {
            return pos;
        }
    }
}
