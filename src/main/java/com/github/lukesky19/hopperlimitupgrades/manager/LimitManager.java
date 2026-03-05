/*
    HopperLimitUpgrades allows the upgrading of hopper limits for BentoBox Islands and the Limits addon.
    Copyright (C) 2024 lukeskywlker19

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.github.lukesky19.hopperlimitupgrades.manager;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.config.Locale;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.limits.Limits;
import world.bentobox.limits.listeners.BlockLimitsListener;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the updating of an island's hopper limit offset.
 */
public class LimitManager {
    private final @NonNull HopperLimitUpgrades hopperLimitUpgrades;
    private final @NonNull LocaleManager localeManager;
    private final @NonNull IslandsManager islandsManager;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     * @param localeManager A {@link LocaleManager} instance.
     */
    public LimitManager(
            @NonNull HopperLimitUpgrades hopperLimitUpgrades,
            @NonNull LocaleManager localeManager) {
        this.hopperLimitUpgrades = hopperLimitUpgrades;
        this.localeManager = localeManager;
        this.islandsManager = BentoBox.getInstance().getIslandsManager();
    }

    /**
     * Set the hopper limit offset for an island.
     * @param sender The {@link CommandSender}.
     * @param targetPlayer The {@link Player} to update their island's hopper limit offset for.
     * @param amount The amount to set the hopper limit offset to.
     * @return true if successful, otherwise false.
     */
    public boolean setHopperLimitOffset(@NonNull CommandSender sender, @NonNull Player targetPlayer, int amount) {
        Locale locale = localeManager.getConfiguration();
        Limits limitsAddon = hopperLimitUpgrades.getLimitsAddon();
        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();

        List<TagResolver.Single> placeholders = new ArrayList<>();
        placeholders.add(Placeholder.parsed("player_name", targetPlayer.getName()));

        if(amount < 0) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.amountMustBePositive()));
            return false;
        }

        Island island = islandsManager.getPrimaryIsland(targetPlayer.getWorld(), targetPlayer.getUniqueId());
        if(island == null) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.islandNotFound(), placeholders));
            return false;
        }
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        islandBlockCount.setBlockLimitsOffset(Material.HOPPER, amount);
        int updatedAmount = islandBlockCount.getBlockLimit(Material.HOPPER) + islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        placeholders.add(Placeholder.parsed("amount", String.valueOf(updatedAmount)));

        targetPlayer.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.hopperLimitUpdated(), placeholders));
        sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.playerHopperLimitUpdated(), placeholders));

        return true;
    }

    /**
     * Add to the hopper limit offset for an island.
     * @param sender The {@link CommandSender}.
     * @param targetPlayer The {@link Player} to update their island's hopper limit offset for.
     * @param amount The amount to add to the hopper limit offset.
     * @return true if successful, otherwise false.
     */
    public boolean addHopperLimitOffset(@NonNull CommandSender sender, @NonNull Player targetPlayer, int amount) {
        Locale locale = localeManager.getConfiguration();
        Limits limitsAddon = hopperLimitUpgrades.getLimitsAddon();
        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();

        List<TagResolver.Single> placeholders = new ArrayList<>();
        placeholders.add(Placeholder.parsed("player_name", targetPlayer.getName()));

        if(amount < 0) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.amountMustBePositive()));
            return false;
        }

        Island island = islandsManager.getPrimaryIsland(targetPlayer.getWorld(), targetPlayer.getUniqueId());
        if(island == null) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.islandNotFound(), placeholders));
            return false;
        }
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        int updatedCount = islandBlockCount.getBlockLimitOffset(Material.HOPPER) + amount;

        islandBlockCount.setBlockLimitsOffset(Material.HOPPER, updatedCount);

        int updatedAmount = islandBlockCount.getBlockLimit(Material.HOPPER) + islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        placeholders.add(Placeholder.parsed("amount", String.valueOf(updatedAmount)));

        targetPlayer.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.hopperLimitUpdated(), placeholders));
        sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.playerHopperLimitUpdated(), placeholders));

        return true;
    }

    /**
     * Remove from the hopper limit offset for an island.
     * @param sender The {@link CommandSender}.
     * @param targetPlayer The {@link Player} to update their island's hopper limit offset for.
     * @param amount The amount to remove from the hopper limit offset.
     * @return true if successful, otherwise false.
     */
    public boolean removeHopperLimitOffset(@NonNull CommandSender sender, @NonNull Player targetPlayer, int amount) {
        Locale locale = localeManager.getConfiguration();
        Limits limitsAddon = hopperLimitUpgrades.getLimitsAddon();
        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();

        List<TagResolver.Single> placeholders = new ArrayList<>();
        placeholders.add(Placeholder.parsed("player_name", targetPlayer.getName()));

        if(amount < 0) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.amountMustBePositive()));
            return false;
        }

        Island island = islandsManager.getPrimaryIsland(targetPlayer.getWorld(), targetPlayer.getUniqueId());
        if(island == null) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.islandNotFound(), placeholders));
            return false;
        }
        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);

        int updatedCount = islandBlockCount.getBlockLimitOffset(Material.HOPPER) - amount;
        if(updatedCount < 0) updatedCount = 0;

        islandBlockCount.setBlockLimitsOffset(Material.HOPPER, updatedCount);

        int updatedAmount = islandBlockCount.getBlockLimit(Material.HOPPER) + islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        placeholders.add(Placeholder.parsed("amount", String.valueOf(updatedAmount)));

        targetPlayer.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.hopperLimitUpdated(), placeholders));
        sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.playerHopperLimitUpdated(), placeholders));

        return true;
    }

    /**
     * Send a message the hopper limit offset for the target player's island.
     * @param sender The {@link CommandSender}.
     * @param targetPlayer The {@link Player} to get their island's hopper limit offset for.
     * @return true if successful, otherwise false.
     */
    public boolean sendHopperLimitOffsetMessage(@NonNull CommandSender sender, @NonNull Player targetPlayer) {
        Locale locale = localeManager.getConfiguration();
        Limits limitsAddon = hopperLimitUpgrades.getLimitsAddon();
        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();

        List<TagResolver.Single> placeholders = new ArrayList<>();
        placeholders.add(Placeholder.parsed("player_name", targetPlayer.getName()));

        Island island = islandsManager.getPrimaryIsland(targetPlayer.getWorld(), targetPlayer.getUniqueId());
        if(island == null) {
            sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.islandNotFound(), placeholders));
            return false;
        }

        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);
        int count = islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        placeholders.add(Placeholder.parsed("amount", String.valueOf(count)));

        sender.sendMessage(AdventureUtil.deserialize(locale.prefix() + locale.playerHopperLimitOffset(), placeholders));

        return true;
    }
}
