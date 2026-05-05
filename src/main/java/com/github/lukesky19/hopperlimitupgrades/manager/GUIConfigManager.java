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
import com.github.lukesky19.skylib.common.api.adventure.AdventureUtility;
import com.github.lukesky19.skylib.common.api.configuration.abstracts.SimpleConfigManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

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
    public void saveDefaultConfiguration() {
        plugin.saveResource("gui.yml", false);
    }

    @Override
    public @Nullable GUIConfig migrateConfiguration(@NonNull GUIConfig guiConfig) {
        switch(guiConfig.version()) {
            case 3 -> {
                // Latest version, do nothing
                return guiConfig;
            }

            case 2 -> {
                return new GUIConfig(
                        3,
                        guiConfig.guiName(),
                        guiConfig.guiType(),
                        guiConfig.filler(),
                        guiConfig.nextPage(),
                        guiConfig.prevPage(),
                        guiConfig.exit(),
                        guiConfig.dummyButtons(),
                        guiConfig.upgradeButtons());
            }

            case 1, 0 -> {
                List<GUIConfig.UpgradeButtonConfig> migratedUpgradeButtons = guiConfig.upgradeButtons().stream()
                        .map(upgradeButtonConfig ->
                                new GUIConfig.UpgradeButtonConfig(
                                        upgradeButtonConfig.purchasableItem(),
                                        upgradeButtonConfig.purchasedItem(),
                                        upgradeButtonConfig.slot(),
                                        upgradeButtonConfig.offsetAmount(),
                                        new GUIConfig.PriceConfig(
                                                upgradeButtonConfig.price(),
                                                null),
                                        null))
                        .toList();

                return new GUIConfig(
                        2,
                        guiConfig.guiName(),
                        guiConfig.guiType(),
                        guiConfig.filler(),
                        guiConfig.nextPage(),
                        guiConfig.prevPage(),
                        guiConfig.exit(),
                        guiConfig.dummyButtons(),
                        migratedUpgradeButtons);
            }

            default -> {
                logger.warn(AdventureUtility.plain("Unable to migrate gui configuration due to an unsupported version: " + guiConfig.version()));
                return null;
            }
        }
    }

    @Override
    public boolean validateConfiguration(@Nullable GUIConfig guiConfig) {
        return guiConfig != null;
    }
}