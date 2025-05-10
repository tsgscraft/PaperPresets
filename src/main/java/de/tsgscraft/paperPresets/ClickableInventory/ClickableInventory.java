package de.tsgscraft.paperPresets.ClickableInventory;

import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItem;
import de.tsgscraft.paperPresets.ClickableInventory.Items.InventoryChangeItems;
import de.tsgscraft.paperPresets.PaperPresets;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

import java.util.*;

public class ClickableInventory implements InventoryHolder {

    private Player player;

    private final Inventory inv;

    @Nullable private ClickableInventory previousInv;

    private Map<Integer, ClickedAction> actionMap = new HashMap<>();
    private Map<UUID, Integer> actionChangeMap = new HashMap<>();
    private List<ChangeItem> changeItems = new ArrayList<>();

    private NamespacedKey actionKey = new NamespacedKey(PaperPresets.getInstance(), "click_action");
    private static NamespacedKey changeKey = new NamespacedKey(PaperPresets.getInstance(), "change_uuid");

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

    public ClickableInventory setItem(ItemPos pos, ChangeItem item, @Nullable ClickedAction action){
        changeItems.add(item);
        if (action != null)
            actionChangeMap.put(item.getUniqueId(), action.hashCode());
        if (item.getSelected() != null) {
            setItem(pos, item.getSelected().getItemStack(), action);
        }else {
            setItem(pos, (ItemStack) item, action);
        }
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

    public ItemStack[] getContents(){
        return inv.getContents();
    }

    public void runAction(ItemStack item, InventoryClickEvent event) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(actionKey, PersistentDataType.INTEGER)) {
            int value = container.get(actionKey, PersistentDataType.INTEGER);
            actionMap.get(value).run(event, this);
        }
    }
/*

                        if (event.getCurrentItem().hasItemMeta()) {
                            NamespacedKey key = ClickableInventory.getChangeKey();
                            PersistentDataContainer container = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
                            if (container.has(key, PersistentDataType.STRING)) {
                                String value = container.get(key, PersistentDataType.STRING);
                                ChangeItemVariant selected = ClickableInventory.getChangeItem(UUID.fromString(value)).getFirst().getSelected();
                                if (selected != null)
                                    ClickableInventory.updateChangeItem(UUID.fromString(value), selected.getVariantID().equals("on") ? "off" : "on");
                            }
                        }
 */
    public static void updateChangeItem(UUID uuid, String variant){
        for (InventoryChangeItems changeItem : getChangeItem(uuid)){
            ChangeItem item = changeItem.getItem();
            item.setActive(variant);
            Integer actionId = changeItem.getClickableInventory().getActionChangeMap().get(item.getUniqueId());
            for (int slot : changeItem.getSlot()) {
                if (actionId != null) {
                    changeItem.getClickableInventory().setItem(new ItemPos(slot), item.getSelected() != null ? item.getSelected().getItemStack() : item, changeItem.getClickableInventory().getActionMap().get(actionId));
                } else {
                    changeItem.getClickableInventory().setItem(new ItemPos(slot), item.getSelected() != null ? item.getSelected().getItemStack() : item, null);
                }
            }
        }
    }

    public interface VariantFilter {
        String filter(ChangeItem item);
    }

    public static void updateChangeItem(InventoryClickEvent event, VariantFilter variant){
        if (event.getCurrentItem().hasItemMeta()) {
            NamespacedKey key = ClickableInventory.getChangeKey();
            PersistentDataContainer container = event.getCurrentItem().getItemMeta().getPersistentDataContainer();
            if (container.has(key, PersistentDataType.STRING)) {
                String value = container.get(key, PersistentDataType.STRING);
                ClickableInventory.updateChangeItem(UUID.fromString(value), variant.filter(ClickableInventory.getChangeItem(UUID.fromString(value)).getFirst().getItem()));
            }
        }
    }

    public static List<InventoryChangeItems> getChangeItem(UUID uuid) {
        List<InventoryChangeItems> output = new ArrayList<>();
        for (Player allPlayers : Bukkit.getOnlinePlayers()){
            if (allPlayers.getOpenInventory().getTopInventory().getHolder(false) instanceof ClickableInventory clickableInventory){
                for (ChangeItem changeItem : clickableInventory.getChangeItems()) {
                    if (changeItem.getUniqueId().equals(uuid)){
                        output.add(new InventoryChangeItems(clickableInventory, changeItem));
                        break;
                    }
                }
            }
        }
        return output;
    }

    public static NamespacedKey getChangeKey(){
        return changeKey;
    }

    private List<ChangeItem> getChangeItems() {
        return changeItems;
    }

    public Map<Integer, ClickedAction> getActionMap() {
        return actionMap;
    }

    public Map<UUID, Integer> getActionChangeMap() {
        return actionChangeMap;
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
