package de.tsgscraft.paperPresets.ClickableInventory.Items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChangeItemVariant {

    private ItemStack itemStack;

    private final String variantID;

    public ChangeItemVariant(Material material, String variantID){
        itemStack = new ItemStack(material);
        this.variantID = variantID;
    }

    public ChangeItemVariant(ItemStack itemStack1, String variantID){
        itemStack = itemStack1;
        this.variantID = variantID;
    }

    public ChangeItemVariant setName(Component name){
        itemStack.editMeta(itemMeta -> itemMeta.customName(name));
        return this;
    }

    public ChangeItemVariant setLore(List<? extends Component> lore){
        itemStack.editMeta(itemMeta -> itemMeta.lore(lore));
        return this;
    }

    public ChangeItemVariant setAmount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    public int getAmount(){
        return itemStack.getAmount();
    }

    public ChangeItemVariant setHideTooltip(boolean hideTooltip){
        itemStack.editMeta(itemMeta -> itemMeta.setHideTooltip(hideTooltip));
        return this;
    }

    public ChangeItemVariant setItemModel(NamespacedKey itemModel){
        itemStack.editMeta(itemMeta -> itemMeta.setItemModel(itemModel));
        return this;
    }

    public ChangeItemVariant setEnchantmentGlintOverride(@Nullable Boolean glintOverride){
        itemStack.editMeta(itemMeta -> itemMeta.setEnchantmentGlintOverride(glintOverride));
        return this;
    }

    public ChangeItemVariant setItemMeta(ItemMeta itemMeta){
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public boolean hasItemMeta(){
        return itemStack.hasItemMeta();
    }

    public ChangeItemVariant editMeta(final @NotNull java.util.function.Consumer<? super ItemMeta> consumer){
        itemStack.editMeta(consumer);
        return this;
    }

    public <M extends ItemMeta> ChangeItemVariant editMeta(final @NotNull Class<M> metaClass, final @NotNull java.util.function.Consumer<@NotNull ? super M> consumer){
        itemStack.editMeta(metaClass, consumer);
        return this;
    }

    public ItemMeta getItemMeta(){
        return itemStack.getItemMeta();
    }

    public String getVariantID() {
        return variantID;
    }

    public @NotNull Material getType() {
        return itemStack.getType();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
