package com.github.lukesky19.hopperlimitupgrades.gui;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.skylib.format.FormatUtil;
import com.github.lukesky19.skylib.gui.GUIButton;
import com.github.lukesky19.skylib.gui.InventoryGUI;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.limits.Limits;
import world.bentobox.limits.listeners.BlockLimitsListener;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpgradeGUI extends InventoryGUI {
    private final HopperLimitUpgrades plugin;
    private final Island island;

    public UpgradeGUI(HopperLimitUpgrades plugin, Player player) {
        this.plugin = plugin;
        this.island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), User.getInstance(player.getUniqueId()));

        createInventory();
        decorate();
    }

    public void createInventory() {
        setInventory(plugin.getServer().createInventory(this, 27, FormatUtil.format("<gold><bold>Hopper Limit Upgrades</bold></gold>")));
    }

    public void decorate() {
        // Set filler
        GUIButton filler = new GUIButton.Builder().setMaterial(Material.GRAY_STAINED_GLASS_PANE).setItemName(FormatUtil.format(" ")).build();

        for (int i = 0; i <= 26; i++) {
            setButton(i, filler);
        }

        GUIButton accent = new GUIButton.Builder().setMaterial(Material.BLUE_STAINED_GLASS_PANE).setItemName(FormatUtil.format(" ")).build();

        setButton(0, accent);
        setButton(8, accent);
        setButton(9, accent);
        setButton(17, accent);
        setButton(18, accent);
        setButton(26, accent);

        // Add exit button
        setButton(22, new GUIButton.Builder()
                .setMaterial(Material.BARRIER)
                .setItemName(FormatUtil.format("<red><bold>Exit the menu.</bold></red>"))
                .setAction(event -> closeInventory(plugin, (Player) event.getWhoClicked()))
                .build());

        // 00 01 02 03 04 05 06 07 08
        // 09 10 11 12 13 14 15 16 17
        // 18 19 20 21 22 23 24 25 26

        // Add upgrades
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

            setButton(11, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>35 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        Economy economy = plugin.getEconomy();

                        if(economy.getBalance(player) >= 75000) {
                            economy.withdrawPlayer(player, 75000);
                            islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 10);
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 35.</dark_green>"));

                            decorate();
                        } else {
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                        }
                    }).build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            setButton(11, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>35 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .build());
        }

        if(hopperLimitOffset < 20) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$90,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 45.</gray>"));

            setButton(12, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>45 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        Economy economy = plugin.getEconomy();

                        if(economy.getBalance(player) >= 90000) {
                            economy.withdrawPlayer(player, 90000);
                            islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 20);
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 45.</dark_green>"));

                            decorate();
                        } else {
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                        }
                    }).build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            setButton(12, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>45 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .build());
        }

        if(hopperLimitOffset < 30) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$105,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 55.</gray>"));

            setButton(13, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>55 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        Economy economy = plugin.getEconomy();

                        if(economy.getBalance(player) >= 105000) {
                            economy.withdrawPlayer(player, 105000);
                            islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 30);
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 55.</dark_green>"));

                            decorate();
                        } else {
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                        }
                    }).build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            setButton(13, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>55 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .build());
        }

        if(hopperLimitOffset < 40) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$135,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 65.</gray>"));

            setButton(14, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>65 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        Economy economy = plugin.getEconomy();

                        if(economy.getBalance(player) >= 135000) {
                            economy.withdrawPlayer(player, 135000);
                            islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 40);
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 65.</dark_green>"));

                            decorate();
                        } else {
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                        }
                    }).build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            setButton(14, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>65 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .build());
        }

        if(hopperLimitOffset < 50) {
            List<Component> lore = List.of(
                    FormatUtil.format("<gray>Cost:</gray> <dark_green>$150,000</dark_green>"),
                    FormatUtil.format(" "),
                    FormatUtil.format("<gray>Click here to upgrade your island hopper limit to 75.</gray>"));

            setButton(15, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>75 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        Economy economy = plugin.getEconomy();

                        if(economy.getBalance(player) >= 150000) {
                            economy.withdrawPlayer(player, 150000);
                            islandBlockCount.setBlockLimitsOffset(Material.HOPPER, 50);
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Upgraded hopper limit to 75.</dark_green>"));

                            decorate();
                        } else {
                            player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><red>You do not have enough money for this upgrade.</red>"));
                        }
                    }).build());
        } else {
            List<Component> lore = List.of(FormatUtil.format("<red>You already have this upgrade.</red>"));

            setButton(15, new GUIButton.Builder()
                    .setMaterial(Material.HOPPER)
                    .setItemName(FormatUtil.format("<aqua>75 Hopper Limit</aqua>"))
                    .setLore(lore)
                    .build());
        }

        super.decorate();
    }
}
