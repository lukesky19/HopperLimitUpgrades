package com.github.lukesky19.hopperlimitupgrades.manager;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.config.Settings;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import com.github.lukesky19.skylib.api.configurate.ConfigurationUtility;
import com.github.lukesky19.skylib.libs.configurate.ConfigurateException;
import com.github.lukesky19.skylib.libs.configurate.serialize.SerializationException;
import com.github.lukesky19.skylib.libs.configurate.yaml.YamlConfigurationLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

/**
 * This class manages everything related to handling the plugin's settings.
 */
public class SettingsManager {
    private final @NotNull HopperLimitUpgrades hopperLimitUpgrades;
    private @Nullable Settings settings;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     */
    public SettingsManager(
            @NotNull HopperLimitUpgrades hopperLimitUpgrades) {
        this.hopperLimitUpgrades = hopperLimitUpgrades;
    }

    /**
     * Get the plugin's {@link Settings}.
     * @return The plugin's {@link Settings} or null.
     */
    public @Nullable Settings getSettings() {
        return settings;
    }

    /**
     * A method to reload the plugin's settings config.
     */
    public void reload() {
        ComponentLogger logger = hopperLimitUpgrades.getComponentLogger();
        settings = null;

        Path path = Path.of(hopperLimitUpgrades.getDataFolder() + File.separator + "settings.yml");
        if(!path.toFile().exists()) {
            hopperLimitUpgrades.saveResource("settings.yml", false);
        }

        YamlConfigurationLoader loader = ConfigurationUtility.getYamlConfigurationLoader(path);
        try {
            settings = loader.load().get(Settings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        } catch (ConfigurateException configurateException) {
            logger.error(AdventureUtil.serialize("<red>Failed to load plugin settings.</red>"));
            if(configurateException.getMessage() != null) {
                logger.error(AdventureUtil.serialize(configurateException.getMessage()));
            }
        }
    }
}
