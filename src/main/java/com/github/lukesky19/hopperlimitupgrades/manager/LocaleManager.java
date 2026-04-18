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
import com.github.lukesky19.hopperlimitupgrades.config.Locale;
import com.github.lukesky19.hopperlimitupgrades.config.Settings;
import com.github.lukesky19.skylib.common.api.adventure.AdventureUtility;
import com.github.lukesky19.skylib.common.api.configuration.abstracts.SimpleConfigManager;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * This class loads the plugin's locale configuration.
 */
public class LocaleManager extends SimpleConfigManager<Locale> {
    private final @NonNull SettingsManager settingsManager;
    private final @NonNull Locale DEFAULT_LOCALE = new Locale(
            2,
            "<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray>",
            "<dark_green>The plugin has been reloaded.</dark_green>",
            List.of("<aqua>HopperLimitUpgrades is developed by <white><bold>lukeskywlker19</bold></white>.</aqua>",
                    "<aqua>Source code is released on GitHub: <click:OPEN_URL:https://github.com/lukesky19><yellow><underlined><bold>https://github.com/lukesky19</bold></underlined></yellow></click></aqua>",
                    " ",
                    "<aqua><bold>List of Commands:</bold></aqua>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>reload</yellow>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>help</yellow>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>reset <player name></yellow>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>set <player name> <amount></yellow>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>add <player name> <amount></yellow>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>remove <player name> <amount></yellow>",
                    "<white>/</white><aqua>hopperlimitupgrades</aqua> <yellow>get <player name></yellow>"),
            "<red>The upgrade GUI can only be open by players.</red>",
            "<red>You must be on an island to open the upgrade GUI.</red>",
            "<red>Only the island owner and island members can open the upgrade GUI.</red>",
            "<red>Unable to open this GUI because of a configuration error.</red>",
            "<red>You do not have enough money for this upgrade.</red>",
            "<red>You do not have enough money for this upgrade.</red>",
            "<red>You do not have enough player points for this upgrade.</red>",
            "<red>Unable to process upgrade due to a server error.</red>",
            "<dark_green>Upgraded hopper limit to <aqua><amount</aqua>.</dark_green>",
            "<dark_green>Your hopper limit was updated to <aqua><amount</aqua>.</dark_green>",
            "<dark_green>Player <aqua><player_name></aqua>'s hopper limit was updated to <aqua><amount></aqua>.</dark_green>",
            "<dark_green><player_name></aqua>'s hopper limit offset is <aqua><amount></aqua>.</dark_green>",
            "<red>The hopper limit amount must be positive.</red>",
            "<red>No island was found for player <aqua><player_name></aqua>.</red>");

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     * @param settingsManager A {@link SettingsManager} instance.
     */
    public LocaleManager(
            @NonNull HopperLimitUpgrades hopperLimitUpgrades,
            @NonNull SettingsManager settingsManager)  {
        super(hopperLimitUpgrades, Locale.class);

        this.settingsManager = settingsManager;
    }

    /**
     * Gets the plugin's locale if not null or the default locale otherwise.
     * @return The plugin's locale if not null or the default locale otherwise.
     */
    @Override
    public @NonNull Locale getConfiguration() {
        if(configuration == null) return DEFAULT_LOCALE;
        return configuration;
    }

    @Override
    public void loadConfiguration() {
        Settings settings = settingsManager.getConfiguration();
        if(settings == null) {
            logger.error(AdventureUtility.deserialize("<red>Failed to load plugin's locale due to plugin settings being null.</red>"));
            return;
        }
        if(settings.locale() == null) {
            logger.error(AdventureUtility.deserialize("<red>Failed to load plugin's locale to use in settings.yml is null.</red>"));
            return;
        }

        String localeString = settings.locale();
        Path path = Path.of(plugin.getDirectoryFile() + File.separator + "locale" + File.separator + (localeString + ".yml"));
        setConfigurationPath(path);

        super.loadConfiguration();
    }

    @Override
    public void saveDefaultConfiguration() {
        Path path = Path.of(plugin.getDirectoryFile() + File.separator + "locale" + File.separator + "en_US.yml");
        if(!path.toFile().exists()) {
            plugin.saveResource("locale" + File.separator + "en_US.yml", false);
        }
    }

    @Override
    public @Nullable Locale migrateConfiguration(@NonNull Locale locale) {
        switch(locale.version()) {
            case 2 -> {
                return locale;
            }

            case 1, 0 -> {
                return new Locale(
                        2,
                        locale.prefix(),
                        locale.reload(),
                        locale.help(),
                        locale.playerOnly(),
                        locale.notOnIsland(),
                        locale.islandMemberOrOwnerOnly(),
                        locale.guiOpenError(),
                        null,
                        locale.insufficientFunds(),
                        "<red>You do not have enough player points for this upgrade.</red>",
                        "<red>Unable to process upgrade due to a server error.</red>",
                        locale.hopperLimitUpgraded(),
                        locale.hopperLimitUpdated(),
                        locale.playerHopperLimitUpdated(),
                        locale.playerHopperLimitOffset(),
                        locale.amountMustBePositive(),
                        locale.islandNotFound());
            }

            default -> {
                logger.warn(AdventureUtility.plain("Unable to migrate locale configuration due to an unsupported version: " + locale.version()));
                return null;
            }
        }
    }

    @Override
    public boolean validateConfiguration(@Nullable Locale locale) {
        if(locale == null) return false;

        if(locale.prefix() == null
                || locale.reload() == null
                || locale.playerOnly() == null
                || locale.notOnIsland() == null
                || locale.islandMemberOrOwnerOnly() == null
                || locale.guiOpenError() == null
                || locale.insufficientMoney() == null
                || locale.insufficientPlayerPoints() == null
                || locale.hopperLimitUpgraded() == null
                || locale.hopperLimitUpdated() == null
                || locale.playerHopperLimitUpdated() == null
                || locale.playerHopperLimitOffset() == null
                || locale.amountMustBePositive() == null
                || locale.islandNotFound() == null) {
            logger.warn(AdventureUtility.deserialize("The plugin's config version or one of the plugin's locale messages is null. Double-check your configuration. The default locale will be used."));
            return false;
        }

        return true;
    }
}