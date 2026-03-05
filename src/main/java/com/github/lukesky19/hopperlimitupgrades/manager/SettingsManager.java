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
import com.github.lukesky19.skylib.api.common.abstracts.config.SimpleConfigManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

/**
 * This class manages everything related to handling the plugin's settings.
 */
public class SettingsManager extends SimpleConfigManager<Settings> {
    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     */
    public SettingsManager(@NonNull HopperLimitUpgrades hopperLimitUpgrades) {
        super(hopperLimitUpgrades, Path.of(hopperLimitUpgrades.getDataFolder() + File.separator + "settings.yml"), Settings.class);
    }

    @Override
    public void saveBundledConfig() {
        plugin.saveResource("settings.yml", false);
    }

    @Override
    public @Nullable Settings migrateConfiguration(@NonNull Settings settings) {
        if(settings.version() == 0) {
            return new Settings(1, settings.locale());
        }

        return settings;
    }

    @Override
    public boolean validateConfiguration(@Nullable Settings configuration) {
        if(configuration == null) return false;

        if(configuration.locale() == null) {
            logger.error(AdventureUtil.deserialize("Your settings.yml is missing a defined locale."));
            logger.info(AdventureUtil.deserialize("You can regenerate your settings file by deleting it or defining the locale to use to resolve the issue."));

            return false;
        }

        return true;
    }
}