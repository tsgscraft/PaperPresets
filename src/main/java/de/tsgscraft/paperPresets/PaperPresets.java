package de.tsgscraft.paperPresets;

import de.tsgscraft.paperPresets.ClickableInventory.ClickedListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public final class PaperPresets extends JavaPlugin {

    private static PaperPresets plugin;

    private static Logger logger;

    private static BukkitScheduler scheduler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = getLogger();
        plugin = this;
        scheduler = this.getServer().getScheduler();
        getServer().getPluginManager().registerEvents(new ClickedListener(), this);
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
}
