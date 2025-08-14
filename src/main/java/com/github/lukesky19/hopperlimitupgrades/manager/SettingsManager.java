/*
    HopperLimitUpgrades allows the upgrading of hopper limits for BentoBox Islands and the Limits addon.
    Copyright (C) 2024 lukeskywlker19

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
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
