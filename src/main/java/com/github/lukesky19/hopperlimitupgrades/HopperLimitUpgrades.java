package com.github.lukesky19.hopperlimitupgrades;

import com.github.lukesky19.hopperlimitupgrades.command.UpgradeCommand;
import com.github.lukesky19.hopperlimitupgrades.listener.InventoryListener;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.skylib.format.FormatUtil;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class HopperLimitUpgrades extends JavaPlugin {
    private GUIManager guiManager;
    private Economy economy;

    public Economy getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        boolean skyLibResult = checkSkyLibVersion();
        if(!skyLibResult) return;
        boolean econResult = setupEconomy();
        if(!econResult) return;

        guiManager = new GUIManager(this);

        this.getServer().getPluginManager().registerEvents(new InventoryListener(guiManager), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(UpgradeCommand.createCommand(this, guiManager),
                        "Command to upgrade and manage hopper limits.", List.of("upgrade", "upgrades", "hopperupgrade", "hopperupgrades")));
    }

    @Override
    public void onDisable() {
        guiManager.closeOpenGUIs(true);
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
    @SuppressWarnings("UnstableApiUsage")
    private boolean checkSkyLibVersion() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        Plugin skyLib = pluginManager.getPlugin("SkyLib");
        if (skyLib != null) {
            String version = skyLib.getPluginMeta().getVersion();
            String[] splitVersion = version.split("\\.");
            int second = Integer.parseInt(splitVersion[1]);

            if(second >= 2) {
                return true;
            }
        }

        this.getComponentLogger().error(FormatUtil.format("SkyLib Version 1.2.0.0 or newer is required to run this plugin."));
        this.getServer().getPluginManager().disablePlugin(this);
        return false;
    }
}
