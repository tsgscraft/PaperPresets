package de.tsgscraft.paperPresets.Configuration;

import de.tsgscraft.paperPresets.PaperPresets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class Configuration {
    private File file = null;
    private File parent = null;
    private YamlConfiguration config = null;
    private boolean autoSave = true;
    private String name;
    private Plugin plugin;
    private String fileName;

    /**
     * This gets called when you activate it.
     * Execute function saveDefault() here.
     */
    public abstract void preload(Plugin plugin, String name, String fileName, File parent);

    public abstract void enabled(Plugin plugin, String name, File file);

    public void saveDefault(String path, boolean replace){
        File file1 = new File(parent, fileName);
        if (file1.exists() && !replace) return;
        plugin.saveResource(path, replace);
        PaperPresets.debugLog(plugin, "Overwritten config: " + name);
    }

    public ConfigurationSection getSection(String path){
        return config.getConfigurationSection(path);
    }

    public boolean contains(String path){
        return config.contains(path);
    }

    public Object get(String path){
        return config.get(path);
    }

    public void set(String path, Object value){
        config.set(path, value);
        if (autoSave){
            save();
        }
    }

    public void save(){
        try {
            config.save(file);
        } catch (IOException e) {
            throw new ConfigurationSaveError(name, e);
        }
        PaperPresets.debugLog(plugin, "Saved config: " + name);
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getName() {
        return name;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public YamlConfiguration getRawConfig() {
        return config;
    }

    public void activate(Plugin plugin, String name, @Nullable File folder){
        PaperPresets.debugLog(plugin, "Activating config: " + name);
        this.plugin = plugin;
        this.name = name;
        this.fileName = name + ".yml";
        this.parent = Objects.requireNonNullElseGet(folder, plugin::getDataFolder);
        preload(plugin, name, fileName, parent);
        file = new File(this.parent, fileName);
        if (!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(file);
        save();
        enabled(plugin, name, file);
        PaperPresets.debugLog(plugin, "Done activating config: " + name);
    }
}
