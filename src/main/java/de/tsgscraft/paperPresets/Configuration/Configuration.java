package de.tsgscraft.paperPresets.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class Configuration {
    private File file = null;
    private YamlConfiguration config = null;
    private boolean autoSave = true;
    private String name;
    private Plugin plugin;

    public abstract void preload();

    public abstract void enabled();

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

    /*
    public Config(String name, File path){
        file = new File(path, name);
        if (!file.exists()) {
            path.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
     */

    public void activate(Plugin plugin, String name, @Nullable File folder){
        preload();
        this.plugin = plugin;
        this.name = name;
        String fileName = name + ".yml";
        file = new File(Objects.requireNonNullElseGet(folder, plugin::getDataFolder), fileName);
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
        enabled();
    }
}
