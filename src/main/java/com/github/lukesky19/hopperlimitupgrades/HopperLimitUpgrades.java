package com.github.lukesky19.hopperlimitupgrades;

import com.github.lukesky19.hopperlimitupgrades.command.UpgradeCommand;
import com.github.lukesky19.hopperlimitupgrades.listener.InventoryListener;
import com.github.lukesky19.hopperlimitupgrades.listener.PluginDisableListener;
import com.github.lukesky19.skylib.format.FormatUtil;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class HopperLimitUpgrades extends JavaPlugin {
    private Economy economy;

    public Economy getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        boolean result = setupEconomy();
        if(!result) {
            this.getComponentLogger().error(FormatUtil.format("No Vault dependency found, disabling plugin..."));

            this.getServer().getPluginManager().disablePlugin(this);

            return;
        }

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PluginDisableListener(), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(UpgradeCommand.createCommand(this),
                        "Command to upgrade and manage hopper limits.", List.of("upgrade", "upgrades", "hopperupgrade", "hopperupgrades")));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.economy = rsp.getProvider();
                return true;
            }
        }

        return false;
    }
}
