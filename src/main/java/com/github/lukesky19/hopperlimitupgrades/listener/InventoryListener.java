package com.github.lukesky19.hopperlimitupgrades.listener;

import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.skylib.gui.abstracts.ChestGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class InventoryListener implements Listener {
    private final GUIManager guiManager;

    public InventoryListener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        UUID uuid = inventoryClickEvent.getWhoClicked().getUniqueId();
        Inventory inventory = inventoryClickEvent.getClickedInventory();

        ChestGUI gui = guiManager.getOpenGUI(uuid);
        if(gui == null) return;

        gui.handleGlobalClick(inventoryClickEvent);

        if(inventory instanceof PlayerInventory) {
            gui.handleBottomClick(inventoryClickEvent);
        } else {
            gui.handleTopClick(inventoryClickEvent);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent inventoryDragEvent) {
        UUID uuid = inventoryDragEvent.getWhoClicked().getUniqueId();
        Inventory inventory = inventoryDragEvent.getInventory();

        ChestGUI gui = guiManager.getOpenGUI(uuid);
        if(gui == null) return;

        gui.handleGlobalDrag(inventoryDragEvent);

        if(inventory instanceof PlayerInventory) {
            gui.handleBottomDrag(inventoryDragEvent);
        } else {
            gui.handleTopDrag(inventoryDragEvent);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
        UUID uuid = inventoryCloseEvent.getPlayer().getUniqueId();

        ChestGUI gui = guiManager.getOpenGUI(uuid);

        if (gui != null) {
            gui.handleClose(inventoryCloseEvent);
        }
    }
}
