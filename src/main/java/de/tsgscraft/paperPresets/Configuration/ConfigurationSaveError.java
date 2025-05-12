package de.tsgscraft.paperPresets.Configuration;

import java.io.IOException;

public class ConfigurationSaveError extends RuntimeException {
    public ConfigurationSaveError(String config, IOException e) {
        super("Failed to save Config: " + config, e);
    }
}
