package de.tsgscraft.paperPresets.Configuration;

import de.tsgscraft.paperPresets.PaperPresets;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ExampleConfig extends Configuration {
    @Override
    public void preload() {

    }

    @Override
    public void enabled() {
        set("test", "asd");
        set("test2", 42);
        set("test3.asd", true);
        PaperPresets.getLog().info(get("test").toString());
    }
}
