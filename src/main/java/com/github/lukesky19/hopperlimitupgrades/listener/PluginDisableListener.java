package com.github.lukesky19.hopperlimitupgrades.listener;

import com.github.lukesky19.hopperlimitupgrades.gui.UpgradeGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

public class PluginDisableListener implements Listener {
    /**
     * A hacky way to make sure any plugin GUIs are closed if Plugman is used.
     * @param event A PluginDisableEvent
     */
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();

        if(plugin.getName().equals("HopperLimitUpgrades")) {
            for(Player player : plugin.getServer().getOnlinePlayers()) {
                InventoryView inventory = player.getOpenInventory();
                Inventory topInventory = inventory.getTopInventory();

                if(topInventory.getHolder(false) instanceof UpgradeGUI) {
                    player.closeInventory(InventoryCloseEvent.Reason.UNLOADED);
                }
            }
        }
    }
}
