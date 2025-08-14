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
package com.github.lukesky19.hopperlimitupgrades;

import com.github.lukesky19.hopperlimitupgrades.command.UpgradeCommand;
import com.github.lukesky19.hopperlimitupgrades.listener.InventoryListener;
import com.github.lukesky19.hopperlimitupgrades.manager.*;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.limits.Limits;

import java.util.List;
import java.util.Optional;

/**
 * The plugin's main class.
 */
public final class HopperLimitUpgrades extends JavaPlugin {
    private SettingsManager settingsManager;
    private LocaleManager localeManager;
    private GUIConfigManager guiConfigManager;
    private GUIManager guiManager;
    private Economy economy;
    private Limits limitsAddon;

    /**
     * Get the server's {@link Economy}.
     * @return The {@link Economy}.
     */
    public @NotNull Economy getEconomy() {
        return economy;
    }

    /**
     * Get the {@link Limits} addon.
     * @return The {@link Limits} addon.
     */
    public @NotNull Limits getLimitsAddon() {
        return limitsAddon;
    }

    /**
     * Default Constructor
     */
    public HopperLimitUpgrades() {}

    /**
     * The method ran when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!checkSkyLibVersion()) return;
        if(!setupEconomy()) return;
        if(!setupLevelAddon()) return;

        settingsManager = new SettingsManager(this);
        localeManager = new LocaleManager(this, settingsManager);
        guiConfigManager = new GUIConfigManager(this);
        guiManager = new GUIManager(this);
        LimitManager limitManager = new LimitManager(this, localeManager);
        UpgradeCommand upgradeCommand = new UpgradeCommand(this, localeManager, guiConfigManager, guiManager, limitManager);

        this.getServer().getPluginManager().registerEvents(new InventoryListener(guiManager), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(upgradeCommand.createCommand(),
                        "Command to upgrade and manage hopper limits.", List.of("upgrade", "upgrades", "hopperupgrade", "hopperupgrades")));

        reload();
    }

    /**
     * The method ran when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        if(guiManager != null) guiManager.closeOpenGUIs(true);
    }

    /**
     * The plugin's main reload method.
     */
    public void reload() {
        settingsManager.reload();
        localeManager.reload();
        guiConfigManager.reload();
    }

    /**
     * Checks for the Limits addon as a dependency.
     */
    private boolean setupLevelAddon() {
        Optional<Addon> optionalAddon = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(optionalAddon.isEmpty()) {
            this.getComponentLogger().error(AdventureUtil.serialize("<red>HopperLimitUpgrades has been disabled due to no Limits addon dependency found!</red>"));

            this.getServer().getPluginManager().disablePlugin(this);

            return false;
        }

        limitsAddon = (Limits) optionalAddon.get();

        return true;
    }

    /**
     * Checks for Vault as a dependency and sets up the Economy instance.
     */
    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.economy = rsp.getProvider();

                return true;
            }
        }

        getComponentLogger().error(MiniMessage.miniMessage().deserialize("<red>HopperLimitUpgrades has been disabled due to no Vault dependency found!</red>"));
        this.getServer().getPluginManager().disablePlugin(this);
        return false;
    }

    /**
     * Checks if the Server has the proper SkyLib version.
     * @return true if it does, false if not.
     */
    private boolean checkSkyLibVersion() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        Plugin skyLib = pluginManager.getPlugin("SkyLib");
        if (skyLib != null) {
            String version = skyLib.getPluginMeta().getVersion();
            String[] splitVersion = version.split("\\.");
            int second = Integer.parseInt(splitVersion[1]);

            if(second >= 3) {
                return true;
            }
        }

        this.getComponentLogger().error(AdventureUtil.serialize("SkyLib Version 1.3.0.0 or newer is required to run this plugin."));
        this.getServer().getPluginManager().disablePlugin(this);
        return false;
    }
}
