package com.github.lukesky19.hopperlimitupgrades.gui;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.skylib.format.FormatUtil;
import com.github.lukesky19.skylib.gui.GUIButton;
import com.github.lukesky19.skylib.gui.GUIType;
import com.github.lukesky19.skylib.gui.abstracts.ChestGUI;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.limits.Limits;
import world.bentobox.limits.listeners.BlockLimitsListener;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.*;

public class UpgradeGUI extends ChestGUI {
    private final HopperLimitUpgrades plugin;
    private final GUIManager guiManager;
    private final Island island;

    public UpgradeGUI(HopperLimitUpgrades plugin, GUIManager guiManager, Player player) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), User.getInstance(player.getUniqueId()));

        createInventory(player, GUIType.CHEST_27, "<gold><bold>Hopper Limit Upgrades</bold></gold>", null);

        update();
    }

    @Override
    public void update() {
        clearButtons();

        createFiller();
        createAccent();
        createExit();
        createUpgrades();

        super.update();
    }

    private void createFiller() {
        GUIButton.Builder fillerBuilder = new GUIButton.Builder();

        ItemStack fillerStack = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = fillerStack.getItemMeta();

        fillerMeta.displayName(FormatUtil.format(" "));

        fillerStack.setItemMeta(fillerMeta);

        fillerBuilder.setItemStack(fillerStack);

        GUIButton fillerButton = fillerBuilder.build();

        for (int i = 0; i <= 26; i++) {
            setButton(i, fillerButton);
        }
    }

    private void createAccent() {
        GUIButton.Builder accentBuilder = new GUIButton.Builder();

        ItemStack accentStack = ItemStack.of(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta accentMeta = accentStack.getItemMeta();

        accentMeta.displayName(FormatUtil.format(" "));

        accentStack.setItemMeta(accentMeta);

        accentBuilder.setItemStack(accentStack);

        GUIButton accentButton = accentBuilder.build();

        setButton(0, accentButton);
        setButton(8, accentButton);
        setButton(9, accentButton);
        setButton(17, accentButton);
        setButton(18, accentButton);
        setButton(26, accentButton);
    }

    private void createExit() {
        // Add exit button
        GUIButton.Builder exitBuilder = new GUIButton.Builder();

        ItemStack exitStack = ItemStack.of(Material.BARRIER);
        ItemMeta exitMeta = exitStack.getItemMeta();

        exitMeta.displayName(FormatUtil.format("<red><bold>Exit the menu.</bold></red>"));

        exitStack.setItemMeta(exitMeta);

        exitBuilder.setItemStack(exitStack);

        exitBuilder.setAction(event -> closeInventory(plugin, (Player) event.getWhoClicked()));

        setButton(22, exitBuilder.build());
    }

    private void createUpgrades() {
        Optional<Addon> limits = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(limits.isEmpty()) return;

        Limits limitsAddon = (Limits) limits.get();

        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);
        int hopperLimitOffset = islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        if(hopperLimitOffset < 10) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$75,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 35.</gray>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>35 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            upgradeBuilder.setAction(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                Economy economy = plugin.getEconomy();

                if (economy.getBalance(player) >= 75000) {
                    economy.withdrawPlayer(player, 75000);
                    islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 10);
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 35.</dark_green>"));

                    update();
                } else {
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                }
            });

            setButton(11, upgradeBuilder.build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>35 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            setButton(11, upgradeBuilder.build());
        }

        if(hopperLimitOffset < 20) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$90,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 45.</gray>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>45 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            upgradeBuilder.setAction(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                Economy economy = plugin.getEconomy();

                if(economy.getBalance(player) >= 90000) {
                    economy.withdrawPlayer(player, 90000);
                    islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 20);
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 45.</dark_green>"));

                    update();
                } else {
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                }
            });

            setButton(12, upgradeBuilder.build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>45 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            setButton(12, upgradeBuilder.build());
        }

        if(hopperLimitOffset < 30) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$105,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 55.</gray>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>55 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            upgradeBuilder.setAction(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                Economy economy = plugin.getEconomy();

                if(economy.getBalance(player) >= 105000) {
                    economy.withdrawPlayer(player, 105000);
                    islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 30);
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 55.</dark_green>"));

                    update();
                } else {
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                }
            });

            setButton(13, upgradeBuilder.build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>55 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            setButton(13, upgradeBuilder.build());
        }

        if(hopperLimitOffset < 40) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$135,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 65.</gray>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>65 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            upgradeBuilder.setAction(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                Economy economy = plugin.getEconomy();

                if(economy.getBalance(player) >= 135000) {
                    economy.withdrawPlayer(player, 135000);
                    islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 40);
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 65.</dark_green>"));

                    update();
                } else {
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                }
            });

            setButton(14, upgradeBuilder.build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>65 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            setButton(14, upgradeBuilder.build());
        }

        if(hopperLimitOffset < 50) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$150,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 75.</gray>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>75 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            upgradeBuilder.setAction(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                Economy economy = plugin.getEconomy();

                if(economy.getBalance(player) >= 150000) {
                    economy.withdrawPlayer(player, 150000);
                    islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 50);
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 75.</dark_green>"));

                    update();
                } else {
                    player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                }
            });

            setButton(15, upgradeBuilder.build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            GUIButton.Builder upgradeBuilder = new GUIButton.Builder();

            ItemStack upgradeStack = ItemStack.of(Material.HOPPER);
            ItemMeta upgradeMeta = upgradeStack.getItemMeta();

            upgradeMeta.displayName(FormatUtil.format("<aqua>75 Hopper Limit</aqua>"));

            upgradeMeta.lore(lore);

            upgradeStack.setItemMeta(upgradeMeta);

            upgradeBuilder.setItemStack(upgradeStack);

            setButton(15, upgradeBuilder.build());
        }
    }

    @Override
    public void openInventory(@NotNull Plugin plugin, @NotNull Player player) {
        super.openInventory(plugin, player);

        guiManager.addOpenGUI(player.getUniqueId(), this);
    }

    @Override
    public void closeInventory(@NotNull Plugin plugin, @NotNull Player player) {
        UUID uuid = player.getUniqueId();

        plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW), 1L);

        guiManager.removeOpenGUI(uuid);
    }

    @Override
    public void handleClose(@NotNull InventoryCloseEvent inventoryCloseEvent) {
        if(inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.UNLOADED) || inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) return;

        Player player = (Player) inventoryCloseEvent.getPlayer();
        UUID uuid = player.getUniqueId();

        guiManager.removeOpenGUI(uuid);
    }
}
