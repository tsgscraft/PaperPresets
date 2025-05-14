package de.tsgscraft.paperPresets.Configuration;

import org.bukkit.plugin.Plugin;

import java.io.File;

public class ExampleConfig extends Configuration {
    @Override
    public void preload(Plugin plugin, String name, String fileName, File parent) {

    }

    @Override
    public void enabled(Plugin plugin, String name, File file) {
        set("test", "asd");
        set("test2", 42);
        set("test3.asd", true);
        //PaperPresets.getLog().info(get("test").toString());
    }
}
