package de.tsgscraft.paperPresets.internals;

import de.tsgscraft.paperPresets.Configuration.Configuration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MainConfig extends Configuration {
    @Override
    public void preload(Plugin plugin, String name, String fileName, File parent) {
        saveDefault("config.yml", false);
    }

    @Override
    public void enabled(Plugin plugin, String name, File file) {

    }
}
