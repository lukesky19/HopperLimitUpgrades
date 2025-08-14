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
import com.github.lukesky19.hopperlimitupgrades.config.GUIConfig;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import com.github.lukesky19.skylib.api.configurate.ConfigurationUtility;
import com.github.lukesky19.skylib.libs.configurate.ConfigurateException;
import com.github.lukesky19.skylib.libs.configurate.yaml.YamlConfigurationLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

/**
 * This class manages the plugin's GUI configuration.
 */
public class GUIConfigManager {
    private final @NotNull HopperLimitUpgrades hopperLimitUpgrades;
    private final @NotNull Path guiConfigPath;
    private @Nullable GUIConfig guiConfig;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     */
    public GUIConfigManager(@NotNull HopperLimitUpgrades hopperLimitUpgrades) {
        this.hopperLimitUpgrades = hopperLimitUpgrades;

        guiConfigPath = Path.of(hopperLimitUpgrades.getDataFolder() + File.separator + "gui.yml");
    }

    /**
     * Get the {@link GUIConfig}. May be null.
     * @return The {@link GUIConfig} or null.
     */
    public @Nullable GUIConfig getGuiConfig() {
        return guiConfig;
    }

    /**
     * (Re-)load the GUI configuration.
     */
    public void reload() {
        ComponentLogger logger = hopperLimitUpgrades.getComponentLogger();
        guiConfig = null;

        saveDefaultConfig();

        YamlConfigurationLoader configurationLoader = ConfigurationUtility.getYamlConfigurationLoader(guiConfigPath);

        try {
            guiConfig = configurationLoader.load().get(GUIConfig.class);
        } catch (ConfigurateException configurateException) {
            logger.error(AdventureUtil.serialize("Failed to load upgrade GUI config. Error:" + configurateException.getMessage()));
        }
    }

    /**
     * Save the default config file if it doesn't exist.
     */
    private void saveDefaultConfig() {
        if(!guiConfigPath.toFile().exists()) {
            hopperLimitUpgrades.saveResource("gui.yml", false);
        }
    }
}
