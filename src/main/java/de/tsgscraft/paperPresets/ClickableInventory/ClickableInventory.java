package de.tsgscraft.paperPresets.ClickableInventory;

import de.tsgscraft.paperPresets.PaperPresets;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickableInventory implements InventoryHolder {

    private Player player;

    private final Inventory inv;

    @Nullable private ClickableInventory previousInv;

    private Map<Integer, ClickedAction> actionMap = new HashMap<>();

    NamespacedKey actionKey = new NamespacedKey(PaperPresets.getInstance(), "click_action");

    public ClickableInventory(Plugin plugin, InventorySize size){
        this.inv = plugin.getServer().createInventory(this, 9* size.getAsInt());
    }

    public ClickableInventory(Plugin plugin, InventorySize size, Component name){
        this.inv = plugin.getServer().createInventory(this, 9* size.getAsInt(), name);
    }

    // name
    public ClickableInventory setItem(ItemPos pos, Material material, Component name, @Nullable ClickedAction action){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.customName(name);
        item.setItemMeta(meta);
        setItem(pos, item, action);
        return this;
    }

    // name, lore
    public ClickableInventory setItem(ItemPos pos, Material material, Component name, List<? extends Component> lore, @Nullable ClickedAction action){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.customName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        setItem(pos, item, action);
        return this;
    }

    // name, amount
    public ClickableInventory setItem(ItemPos pos, Material material, Component name, int amount, @Nullable ClickedAction action){
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.customName(name);
        item.setItemMeta(meta);
        setItem(pos, item, action);
        return this;
    }

    // name, lore, amount
    public ClickableInventory setItem(ItemPos pos, Material material, Component name, List<? extends Component> lore, int amount, @Nullable ClickedAction action){
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.customName(name);
        meta.lore(lore);
        item.setItemMeta(meta);
        setItem(pos, item, action);
        return this;
    }

    public ClickableInventory setItem(ItemPos pos, Material material, @Nullable ClickedAction action){
        ItemStack item = new ItemStack(material);
        setItem(pos, item, action);
        return this;
    }

    public ClickableInventory setItem(ItemPos pos, ItemStack item, @Nullable ClickedAction action){
        if (action != null) {
            item.editMeta(meta -> {
                meta.getPersistentDataContainer().set(actionKey, PersistentDataType.INTEGER, action.hashCode());
            });
            actionMap.put(action.hashCode(), action);
        }
        inv.setItem(pos.getAsIndex(), item);
        return this;
    }

    public ClickableInventory build(Player player){
        this.player = player;
        openInventory();
        return this;
    }

    public ClickableInventory build(Player player, ClickableInventory oldInv){
        setPreviousInv(oldInv);
        return build(player);
    }

    public void setPreviousInv(@Nullable ClickableInventory previousInv) {
        this.previousInv = previousInv;
    }

    public @Nullable ClickableInventory getPreviousInv() {
        return previousInv;
    }

    public boolean hasPreviousInv(){
        return previousInv != null;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public void openInventory() {
        player.openInventory(inv);
    }

    public void runAction(ItemStack item, InventoryClickEvent event) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(actionKey, PersistentDataType.INTEGER)) {
            int value = container.get(actionKey, PersistentDataType.INTEGER);
            actionMap.get(value).run(event, this);
        }
    }

    @Override
    public String toString() {
        return "ClickableInventory{" +
                "player=" + player +
                ", inv=" + inv +
                ", previousInv=" + previousInv +
                ", actionMap=" + actionMap +
                ", actionKey=" + actionKey +
                '}';
    }
}
