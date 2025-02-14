package com.github.lukesky19.hopperlimitupgrades.manager;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.skylib.gui.abstracts.ChestGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class GUIManager {
    private final HopperLimitUpgrades hopperLimitUpgrades;
    private final HashMap<UUID, ChestGUI> openGUIs = new HashMap<>();

    public GUIManager(HopperLimitUpgrades hopperLimitUpgrades) {
        this.hopperLimitUpgrades = hopperLimitUpgrades;
    }

    @Nullable
    public ChestGUI getOpenGUI(@NotNull UUID uuid) {
        return openGUIs.get(uuid);
    }

    public void addOpenGUI(@NotNull UUID uuid, @NotNull ChestGUI chestGui) {
        hopperLimitUpgrades.getServer().getScheduler().runTaskLater(hopperLimitUpgrades, () -> openGUIs.put(uuid, chestGui), 1L);
    }

    public void removeOpenGUI(@NotNull UUID uuid) {
        hopperLimitUpgrades.getServer().getScheduler().runTaskLater(hopperLimitUpgrades, () -> openGUIs.remove(uuid), 1L);
    }

    public void closeOpenGUIs(boolean onDisable) {
        if(!onDisable) {
            for(UUID uuid : openGUIs.keySet()) {
                Player player = hopperLimitUpgrades.getServer().getPlayer(uuid);

                if (player != null && player.isConnected() && player.isOnline()) {
                    hopperLimitUpgrades.getServer().getScheduler().runTaskLater(hopperLimitUpgrades, () ->
                            player.closeInventory(InventoryCloseEvent.Reason.UNLOADED), 1L);
                }
            }
        } else {
            for(UUID uuid : openGUIs.keySet()) {
                Player player = hopperLimitUpgrades.getServer().getPlayer(uuid);

                if (player != null && player.isConnected() && player.isOnline()) {
                    player.closeInventory(InventoryCloseEvent.Reason.UNLOADED);
                }
            }
        }

        openGUIs.clear();
    }
}
