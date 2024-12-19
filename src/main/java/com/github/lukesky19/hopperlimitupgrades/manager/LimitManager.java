package com.github.lukesky19.hopperlimitupgrades.manager;

import com.github.lukesky19.skylib.format.FormatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.limits.Limits;
import world.bentobox.limits.listeners.BlockLimitsListener;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.Objects;
import java.util.Optional;

public class LimitManager {
    public static boolean setHopperLimitOffset(CommandSender sender, Player player, int amount) {
        if(amount < 0) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>The amount must be a positive integer.</red>"));
            return false;
        }

        User user = User.getInstance(player.getUniqueId());
        Island island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), user);
        if(island == null) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit because the player's island can't be found.</red>"));
            return false;
        }

        Optional<Addon> limits = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(limits.isEmpty()) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit due to missing the BentoBox limits addon.</red>"));
            return false;
        }

        Limits limitsAddon = (Limits) limits.get();

        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        islandBlockCount.setBlockLimitsOffset(Material.HOPPER, amount);

        sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><dark_green>Set player <aqua>" + player.getName() + "</aqua>'s hopper limit to <aqua>" + amount + "</aqua>. Updated amount: <aqua>" + (islandBlockCount.getBlockLimit(Material.HOPPER) + islandBlockCount.getBlockLimitOffset(Material.HOPPER)) + "</aqua>.</dark_green>"));
        player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Your hopper limit has been set to <aqua>" + (islandBlockCount.getBlockLimit(Material.HOPPER) + islandBlockCount.getBlockLimitOffset(Material.HOPPER)) + "</aqua>.</dark_green>"));

        return true;
    }

    public static boolean addHopperLimitOffset(CommandSender sender, Player player, int amount) {
        if(amount < 0) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>The amount must be a positive integer.</red>"));
            return false;
        }

        User user = User.getInstance(player.getUniqueId());
        Island island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), user);
        if(island == null) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit because the player's island can't be found.</red>"));
            return false;
        }

        Optional<Addon> limits = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(limits.isEmpty()) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit due to missing the BentoBox limits addon.</red>"));
            return false;
        }

        Limits limitsAddon = (Limits) limits.get();

        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        int updatedCount = islandBlockCount.getBlockLimitOffset(Material.HOPPER) + amount;
        if(updatedCount < 0) updatedCount = 0;

        islandBlockCount.setBlockLimitsOffset(Material.HOPPER, updatedCount);

        sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><dark_green>Added <aqua>" + amount + "</aqua> to player <aqua>" + player.getName() + "</aqua>'s hopper limit. Updated amount: <aqua>" + (islandBlockCount.getBlockLimit(Material.HOPPER) + updatedCount) + "</aqua>.</dark_green>"));
        player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Your hopper limit has been set to <aqua>" + (islandBlockCount.getBlockLimit(Material.HOPPER) + updatedCount) + "</aqua>.</dark_green>"));

        return true;
    }

    public static boolean removeHopperLimitOffset(CommandSender sender, Player player, int amount) {
        if(amount < 0) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>The amount must be a positive integer.</red>"));
            return false;
        }

        User user = User.getInstance(player.getUniqueId());
        Island island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), user);
        if(island == null) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit because the player's island can't be found. They may not be on their island</red>"));
            return false;
        }

        Optional<Addon> limits = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(limits.isEmpty()) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit due to missing the BentoBox limits addon.</red>"));
            return false;
        }

        Limits limitsAddon = (Limits) limits.get();

        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        int updatedCount = islandBlockCount.getBlockLimitOffset(Material.HOPPER) - amount;
        if(updatedCount < 0) updatedCount = 0;

        islandBlockCount.setBlockLimitsOffset(Material.HOPPER, updatedCount);

        sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><dark_green>Removed <aqua>" + amount + "</aqua> from player <aqua>" + player.getName() + "</aqua>'s hopper limit. Updated amount: <aqua>" + (islandBlockCount.getBlockLimit(Material.HOPPER) + updatedCount) + "</aqua>.</dark_green>"));
        player.sendMessage(FormatUtil.format("<gold><bold>Upgrades</bold></gold><gray> ▪ </gray><dark_green>Your hopper limit has been set to <aqua>" + (islandBlockCount.getBlockLimit(Material.HOPPER) + updatedCount) + "</aqua>.</dark_green>"));

        return true;
    }

    public static boolean getHopperLimitOffset(CommandSender sender, Player player) {
        User user = User.getInstance(player.getUniqueId());
        Island island = BentoBox.getInstance().getIslands().getIsland(Objects.requireNonNull(Bukkit.getWorld("bskyblock_world")), user);
        if(island == null) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't get hopper limit because the player's island can't be found.</red>"));
            return false;
        }

        Optional<Addon> limits = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(limits.isEmpty()) {
            sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><red>Can't change hopper limit due to missing the BentoBox limits addon.</red>"));
            return false;
        }

        Limits limitsAddon = (Limits) limits.get();

        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        int count = islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        sender.sendMessage(FormatUtil.format("<gold><bold>HopperLimitUpgrades</bold></gold><gray> ▪ </gray><dark_green><aqua>" + player.getName() + "</aqua>'s hopper limit offset is <aqua>" + count + "</aqua>.</dark_green>"));

        return true;
    }
}
