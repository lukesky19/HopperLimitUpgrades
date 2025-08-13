package com.github.lukesky19.hopperlimitupgrades.listener;

import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.skylib.api.gui.interfaces.BaseGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * This class listens for when a plugin GUI is clicked or closed.
 */
public class InventoryListener implements Listener {
    private final @NotNull GUIManager guiManager;

    /**
     * Constructor
     * @param guiManager A {@link GUIManager} instance.
     */
    public InventoryListener(@NotNull GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    /**
     * When an inventory is clicked, check if the Inventory is a GUI created by the plugin.
     * If so, call the handleClick method for the specific GUI.
     * @param inventoryClickEvent InventoryClickEvent
     */
    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        UUID uuid = inventoryClickEvent.getWhoClicked().getUniqueId();
        Inventory inventory = inventoryClickEvent.getClickedInventory();

        @NotNull Optional<@NotNull BaseGUI> optionalBaseGUI = guiManager.getOpenGUI(uuid);
        if (optionalBaseGUI.isEmpty()) return;
        BaseGUI baseGUI = optionalBaseGUI.get();

        baseGUI.handleGlobalClick(inventoryClickEvent);

        if (inventory instanceof PlayerInventory) {
            baseGUI.handleBottomClick(inventoryClickEvent);
        } else {
            baseGUI.handleTopClick(inventoryClickEvent);
        }
    }

    /**
     * When an inventory is dragged, check if the Inventory is a GUI created by the plugin.
     * If so, call the handleDrag method for the specific GUI.
     * @param inventoryDragEvent InventoryClickEvent
     */
    @EventHandler
    public void onDrag(InventoryDragEvent inventoryDragEvent) {
        UUID uuid = inventoryDragEvent.getWhoClicked().getUniqueId();
        Inventory inventory = inventoryDragEvent.getInventory();

        @NotNull Optional<@NotNull BaseGUI> optionalBaseGUI = guiManager.getOpenGUI(uuid);
        if (optionalBaseGUI.isEmpty()) return;
        BaseGUI baseGUI = optionalBaseGUI.get();

        baseGUI.handleGlobalDrag(inventoryDragEvent);

        if (inventory instanceof PlayerInventory) {
            baseGUI.handleBottomDrag(inventoryDragEvent);
        } else {
            baseGUI.handleTopDrag(inventoryDragEvent);
        }
    }

    /**
     * When an inventory is closed, check if the inventory is a GUI created by the plugin.
     * If so, call the handleClose method for the specific GUI.
     * @param inventoryCloseEvent InventoryCloseEvent
     */
    @EventHandler
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
        UUID uuid = inventoryCloseEvent.getPlayer().getUniqueId();

        @NotNull Optional<@NotNull BaseGUI> optionalBaseGUI = guiManager.getOpenGUI(uuid);
        if (optionalBaseGUI.isEmpty()) return;
        BaseGUI baseGUI = optionalBaseGUI.get();

        baseGUI.handleClose(inventoryCloseEvent);
    }
}
