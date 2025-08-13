package com.github.lukesky19.hopperlimitupgrades.manager;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.config.Locale;
import com.github.lukesky19.hopperlimitupgrades.config.Settings;
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
 * This class loads the plugin's locale configuration.
 */
public class LocaleManager {
    private final @NotNull HopperLimitUpgrades hopperLimitUpgrades;
    private final @NotNull SettingsManager settingsManager;
    private final @NotNull Locale DEFAULT_LOCALE = new Locale(
            "1.0.0.0",
            "<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray>",
            "<dark_green>The plugin has been reloaded.</dark_green>",
            "<red>The upgrade GUI can only be open by players.</red>",
            "<red>You must be on an island to open the upgrade GUI.</red>",
            "<red>Only the island owner and island members can open the upgrade GUI.</red>",
            "<red>Unable to open this GUI because of a configuration error.</red>",
            "<red>You do not have enough money for this upgrade.</red>",
            "<dark_green>Upgraded hopper limit to <aqua><amount</aqua>.</dark_green>",
            "<dark_green>Your hopper limit was updated to <aqua><amount</aqua>.</dark_green>",
            "<dark_green>Player <aqua><player_name></aqua>'s hopper limit was updated to <aqua><amount></aqua>.</dark_green>",
            "<dark_green><player_name></aqua>'s hopper limit offset is <aqua><amount></aqua>.</dark_green>",
            "<red>The hopper limit amount must be positive.</red>",
            "<red>No island was found for player <aqua><player_name></aqua>.</red>");
    private @Nullable Locale locale;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     * @param settingsManager A {@link SettingsManager} instance.
     */
    public LocaleManager(
            @NotNull HopperLimitUpgrades hopperLimitUpgrades,
            @NotNull SettingsManager settingsManager)  {
        this.hopperLimitUpgrades = hopperLimitUpgrades;
        this.settingsManager = settingsManager;
    }

    /**
     * Gets the plugin's {@link Locale} or the default locale if it failed to load.
     * @return The plugin's {@link Locale} or the default locale if it failed to load.
     */
    public @NotNull Locale getLocale() {
        if(locale == null) return DEFAULT_LOCALE;
        return locale;
    }

    /**
     * Reloads the plugin's locale.
     */
    public void reload() {
        ComponentLogger logger = hopperLimitUpgrades.getComponentLogger();
        locale = null;

        copyDefaultLocales();

        Settings settings = settingsManager.getSettings();
        if(settings == null) {
            logger.error(AdventureUtil.serialize("<red>Failed to load plugin's locale due to plugin settings being null.</red>"));
            return;
        }
        if(settings.locale() == null) {
            logger.error(AdventureUtil.serialize("<red>Failed to load plugin's locale to use in settings.yml is null.</red>"));
            return;
        }

        String localeString = settings.locale();
        Path path = Path.of(hopperLimitUpgrades.getDataFolder() + File.separator + "locale" + File.separator + (localeString + ".yml"));

        YamlConfigurationLoader loader = ConfigurationUtility.getYamlConfigurationLoader(path);
        try {
            locale = loader.load().get(Locale.class);
        } catch (ConfigurateException exception) {
            throw new RuntimeException(exception);
        }

        validateLocale();
    }

    /**
     * Copies the default locale files that come bundled with the plugin, if they do not exist at least.
     */
    private void copyDefaultLocales() {
        Path path = Path.of(hopperLimitUpgrades.getDataFolder() + File.separator + "locale" + File.separator + "en_US.yml");
        if (!path.toFile().exists()) {
            hopperLimitUpgrades.saveResource("locale" + File.separator + "en_US.yml", false);
        }
    }

    /**
     * Checks if the locale configuration has any null-values.
     */
    private void validateLocale() {
        ComponentLogger logger = hopperLimitUpgrades.getComponentLogger();
        if(locale == null) return;

        if(locale.configVersion() == null
                || locale.prefix() == null
                || locale.reload() == null
                || locale.playerOnly() == null
                || locale.notOnIsland() == null
                || locale.islandMemberOrOwnerOnly() == null
                || locale.guiOpenError() == null
                || locale.insufficientFunds() == null
                || locale.hopperLimitUpgraded() == null
                || locale.hopperLimitUpdated() == null
                || locale.playerHopperLimitUpdated() == null
                || locale.playerHopperLimitOffset() == null
                || locale.amountMustBePositive() == null
                || locale.islandNotFound() == null) {
            logger.warn(AdventureUtil.serialize("The plugin's config version or one of the plugin's locale messages is null. Double-check your configuration. The default locale will be used."));
            locale = null;
        }
    }
}
