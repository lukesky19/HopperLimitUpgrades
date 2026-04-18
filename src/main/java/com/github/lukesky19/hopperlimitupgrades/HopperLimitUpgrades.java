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
import com.github.lukesky19.hopperlimitupgrades.integration.HookManager;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.LimitsAddonHook;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIConfigManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LimitManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LocaleManager;
import com.github.lukesky19.hopperlimitupgrades.manager.SettingsManager;
import com.github.lukesky19.skylib.common.api.adventure.AdventureUtility;
import com.github.lukesky19.skylib.paper.api.gui.impl.UUIDGUIListener;
import com.github.lukesky19.skylib.paper.api.gui.impl.UUIDGUIManager;
import com.github.lukesky19.skylib.paper.api.plugin.SkyPlugin;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.List;

/**
 * The plugin's main class.
 */
public final class HopperLimitUpgrades extends SkyPlugin {
    private SettingsManager settingsManager;
    private LocaleManager localeManager;
    private GUIConfigManager guiConfigManager;
    private UUIDGUIManager guiManager;

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

        HookManager hookManager = new HookManager(this);
        LimitsAddonHook limitsAddonHook = hookManager.getHook(LimitsAddonHook.class);
        if(!limitsAddonHook.isHooked()) return;

        settingsManager = new SettingsManager(this);
        localeManager = new LocaleManager(this, settingsManager);
        guiConfigManager = new GUIConfigManager(this);
        guiManager = new UUIDGUIManager();
        LimitManager limitManager = new LimitManager(localeManager, hookManager);
        UpgradeCommand upgradeCommand = new UpgradeCommand(this, localeManager, guiConfigManager, guiManager, limitManager, hookManager);

        this.getServer().getPluginManager().registerEvents(new UUIDGUIListener(guiManager), this);

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
    @Override
    public void reload() {
        settingsManager.loadConfiguration();
        localeManager.loadConfiguration();
        guiConfigManager.loadConfiguration();
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
            int first = Integer.parseInt(splitVersion[0]);

            if(first >= 2) {
                return true;
            }
        }

        this.getComponentLogger().error(AdventureUtility.plain("SkyLib Version 2.0.0.0 or newer is required to run this plugin."));
        this.getServer().getPluginManager().disablePlugin(this);
        return false;
    }
}
