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
import com.github.lukesky19.skylib.api.common.abstracts.config.SimpleConfigManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

/**
 * This class manages the plugin's GUI configuration.
 */
public class GUIConfigManager extends SimpleConfigManager<GUIConfig> {
    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     */
    public GUIConfigManager(@NonNull HopperLimitUpgrades hopperLimitUpgrades) {
        super(hopperLimitUpgrades, Path.of(hopperLimitUpgrades.getDataFolder() + File.separator + "gui.yml"), GUIConfig.class);
    }

    @Override
    protected void saveBundledConfig() {
        plugin.saveResource("gui.yml", false);
    }

    @Override
    public @Nullable GUIConfig migrateConfiguration(@NonNull GUIConfig guiConfig) {
        if(guiConfig.version() == 0) {
            return new GUIConfig(
                    1,
                    guiConfig.guiName(),
                    guiConfig.guiType(),
                    guiConfig.filler(),
                    guiConfig.nextPage(),
                    guiConfig.prevPage(),
                    guiConfig.exit(),
                    guiConfig.dummyButtons(),
                    guiConfig.upgradeButtons());
        }

        return guiConfig;
    }

    @Override
    public boolean validateConfiguration(@Nullable GUIConfig guiConfig) {
        return guiConfig != null;
    }
}