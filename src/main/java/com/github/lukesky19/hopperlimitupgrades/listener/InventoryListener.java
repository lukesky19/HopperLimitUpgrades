package com.github.lukesky19.hopperlimitupgrades.listener;

import com.github.lukesky19.hopperlimitupgrades.gui.UpgradeGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if(inventory == null) return;

        if(inventory.getHolder(false) instanceof UpgradeGUI upgradeGUI) {
            upgradeGUI.handleClick(event);
        }
    }
}
