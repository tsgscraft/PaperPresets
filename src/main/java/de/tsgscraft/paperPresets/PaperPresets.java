package de.tsgscraft.paperPresets;

import de.tsgscraft.paperPresets.ClickableInventory.ClickableInventory;
import de.tsgscraft.paperPresets.ClickableInventory.ClickedAction;
import de.tsgscraft.paperPresets.ClickableInventory.ClickedListener;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItem;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItemBuilder;
import de.tsgscraft.paperPresets.ClickableInventory.Items.ChangeItemVariant;
import de.tsgscraft.paperPresets.Configuration.ExampleConfig;
import de.tsgscraft.paperPresets.internals.MainConfig;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public final class PaperPresets extends JavaPlugin {

    private static PaperPresets plugin;

    private static Logger logger;

    private static BukkitScheduler scheduler;

    private static ChangeItem securityDisableItem;
    private static ClickedAction securityDisableAction;

    private static ChangeItem securityResetItem;
    private static ClickedAction securityResetAction;

    private static ChangeItem securityActivateItem;
    private static ClickedAction securityActivateAction;

    private static boolean enabledDebugLogging = true;
    private static boolean enableTestCommand = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = getLogger();
        plugin = this;
        scheduler = this.getServer().getScheduler();

        MainConfig config = new MainConfig();
        config.activate(this, "config", null);

        enabledDebugLogging = (boolean) config.get("debug_log");
        enableTestCommand = (boolean) config.get("enable_example_command");

        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.activate(this, "test", null);

        getServer().getPluginManager().registerEvents(new ClickedListener(), this);

        createSecurityReset();
        createSecurityActivate();
        createSecurityOnOff();

        TranslationStore.StringBased<MessageFormat> store = TranslationStore.messageFormat(Key.key("namespace:value"));

        List<Locale> locales = new ArrayList<>();
        locales.add(Locale.US);
        locales.add(Locale.GERMANY);
        for (Locale locale : locales){
            ResourceBundle bundle = ResourceBundle.getBundle("paper_presets.lang.Lang", locale, UTF8ResourceBundleControl.get());
            store.registerAll(locale, bundle, true);
            GlobalTranslator.translator().addSource(store);
        }
    }

    private void createSecurityActivate(){
        ChangeItemVariant variantOn = new ChangeItemVariant(Material.LIME_WOOL, "on").setName(Component.text("§aON"));
        ChangeItemVariant variantOff = new ChangeItemVariant(Material.RED_WOOL, "off").setName(Component.text("§cOFF"));

        securityActivateItem = new ChangeItemBuilder(Material.BLACK_WOOL, getInstance())
                .setName(Component.text("test"))
                .addVariants(variantOn, variantOff)
                .build();

        World world = Bukkit.getWorld("world");

        Location loc = new Location(world, 97, 87, -114);

        securityActivateItem.setActive("off");

        securityActivateAction = (event, inv) -> ClickableInventory.updateChangeItem(event, item -> {
            loc.getBlock().setType(Material.REDSTONE_BLOCK);
            scheduler.runTaskLater(this, bukkitTask -> {
                ClickableInventory.updateChangeItem(item.getUniqueId(), "off");
                loc.getBlock().setType(Material.RED_WOOL);
            }, 20);
            return "on";
        });
    }

    private void createSecurityReset(){
        ChangeItemVariant variantOn = new ChangeItemVariant(Material.LIME_WOOL, "on").setName(Component.text("§aON"));
        ChangeItemVariant variantOff = new ChangeItemVariant(Material.RED_WOOL, "off").setName(Component.text("§cOFF"));

        securityResetItem = new ChangeItemBuilder(Material.BLACK_WOOL, getInstance())
                .setName(Component.text("test"))
                .addVariants(variantOn, variantOff)
                .build();

        World world = Bukkit.getWorld("world");

        Location loc = new Location(world, 99, 87, -114);

        securityResetItem.setActive("off");

        securityResetAction = (event, inv) -> ClickableInventory.updateChangeItem(event, item -> {
            loc.getBlock().setType(Material.REDSTONE_BLOCK);
            scheduler.runTaskLater(this, bukkitTask -> {
                ClickableInventory.updateChangeItem(item.getUniqueId(), "off");
                loc.getBlock().setType(Material.RED_WOOL);
            }, 20);
            return "on";
        });
    }

    private void createSecurityOnOff(){
        ChangeItemVariant variantOn = new ChangeItemVariant(Material.LIME_WOOL, "on").setName(Component.text("§aON"));
        ChangeItemVariant variantOff = new ChangeItemVariant(Material.RED_WOOL, "off").setName(Component.text("§cOFF"));

        securityDisableItem = new ChangeItemBuilder(Material.BLACK_WOOL, getInstance())
                .setName(Component.text("test"))
                .addVariants(variantOn, variantOff)
                .build();

        World world = Bukkit.getWorld("world");

        Location loc = new Location(world, 101, 87, -114);

        securityDisableItem.setActive(loc.getBlock().getType().equals(Material.RED_WOOL) ? "on" : "off");

        securityDisableAction = (event, inv) -> ClickableInventory.updateChangeItem(event, item -> {
            ChangeItemVariant selected = item.getSelected();
            if (selected != null) {
                loc.getBlock().setType(item.getSelected().getVariantID().equals("on") ? Material.REDSTONE_BLOCK : Material.RED_WOOL);
                return item.getSelected().getVariantID().equals("on") ? "off" : "on";
            }
            return "off";
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PaperPresets getInstance(){
        return plugin;
    }

    public static Logger getLog() {
        return logger;
    }

    public static BukkitScheduler getScheduler() {
        return scheduler;
    }

    public static ChangeItem getSecurityActivateItem() {
        return securityActivateItem;
    }

    public static ChangeItem getSecurityDisableItem() {
        return securityDisableItem;
    }

    public static ChangeItem getSecurityResetItem() {
        return securityResetItem;
    }

    public static ClickedAction getSecurityActivateAction() {
        return securityActivateAction;
    }

    public static ClickedAction getSecurityDisableAction() {
        return securityDisableAction;
    }

    public static ClickedAction getSecurityResetAction() {
        return securityResetAction;
    }

    public static void debugLog(String msg){
        debugLog(plugin, msg);
    }

    public static void debugLog(Plugin plugin, String msg){
        if (enabledDebugLogging){
            logger.info(plugin.getName() + " >> " + msg);
        }
    }

    public static void globalDebugLog(String msg){
        if (enabledDebugLogging){
            logger.info(" >> " + msg);
        }
    }
}
