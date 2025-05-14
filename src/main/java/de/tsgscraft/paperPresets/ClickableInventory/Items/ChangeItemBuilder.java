package de.tsgscraft.paperPresets.ClickableInventory.Items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeItemBuilder {

    private Plugin plugin;

    private ItemStack itemStack;

    private Map<String, ChangeItemVariant> variants = new HashMap<>();

    private Component name;
    private List<? extends Component> lore;
    private int amount;
    private Boolean hideTooltip;
    private NamespacedKey itemModel;
    private @Nullable Boolean glintOverride = null;

    public ChangeItemBuilder(Material fallback, Plugin plugin){
        this.plugin = plugin;
        itemStack = new ItemStack(fallback);
    }

    public ChangeItemBuilder setName(Component name){
        itemStack.editMeta(itemMeta -> itemMeta.customName(name));
        this.name = name;
        return this;
    }

    public ChangeItemBuilder setLore(List<? extends Component> lore){
        itemStack.editMeta(itemMeta -> itemMeta.lore(lore));
        this.lore = lore;
        return this;
    }

    public ChangeItemBuilder setAmount(int amount){
        itemStack.setAmount(amount);
        this.amount = amount;
        return this;
    }

    public ChangeItemBuilder setHideTooltip(boolean hideTooltip){
        itemStack.editMeta(itemMeta -> itemMeta.setHideTooltip(hideTooltip));
        this.hideTooltip = hideTooltip;
        return this;
    }

    public ChangeItemBuilder setItemModel(NamespacedKey itemModel){
        itemStack.editMeta(itemMeta -> itemMeta.setItemModel(itemModel));
        this.itemModel = itemModel;
        return this;
    }

    public ChangeItemBuilder setEnchantmentGlintOverride(@Nullable Boolean glintOverride){
        itemStack.editMeta(itemMeta -> itemMeta.setEnchantmentGlintOverride(glintOverride));
        this.glintOverride = glintOverride;
        return this;
    }

    public ChangeItemBuilder setItemMeta(ItemMeta itemMeta){
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemMeta getItemMeta(){
        return itemStack.getItemMeta();
    }

    public ChangeItemBuilder setVariants(Map<String, ChangeItemVariant> variants) {
        this.variants = variants;
        return this;
    }

    public ChangeItemBuilder addVariants(ChangeItemVariant ...variants){
        for (ChangeItemVariant variant : variants){
            this.variants.put(variant.getVariantID(), variant);
        }
        return this;
    }

    public Map<String, ChangeItemVariant> getVariants() {
        return variants;
    }

    public @Nullable Boolean getGlintOverride() {
        return glintOverride;
    }

    public Component getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getAmount() {
        return amount;
    }

    public List<? extends Component> getLore() {
        return lore;
    }

    public NamespacedKey getItemModel() {
        return itemModel;
    }

    public Boolean getHideTooltip() {
        return hideTooltip;
    }

    public ChangeItem build(){
        return new ChangeItem(this, plugin);
    }

    public @NotNull ItemStack getFallback() {
        return itemStack;
    }
}
