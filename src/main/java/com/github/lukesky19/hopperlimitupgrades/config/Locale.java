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
package com.github.lukesky19.hopperlimitupgrades.config;

import com.github.lukesky19.skylib.libs.configurate.objectmapping.ConfigSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The plugin's locale configuration.
 * @param configVersion The config version of the locale.
 * @param prefix The plugin's prefix.
 * @param reload The message sent when the plugin is reloaded.
 * @param help The plugin's help messages.
 * @param playerOnly The message sent when a command is player-only.
 * @param notOnIsland The message sent when a player is not on an island.
 * @param islandMemberOrOwnerOnly The message sent when a player is not the island owner or an island member.
 * @param guiOpenError The message sent when the upgrade GUI fails to open.
 * @param insufficientFunds The message sent when a player lacks the funds to purchase an upgrade.
 * @param hopperLimitUpgraded The message sent when a player's island hopper limit has been upgraded.
 * @param hopperLimitUpdated The message sent to a player whose island's hopper limit was manually updated.
 * @param playerHopperLimitUpdated The message sent to the command sender who updated another player's island's hopper limit.
 * @param playerHopperLimitOffset The message sent to the command sender viewing another player's island's hopper limit.
 * @param amountMustBePositive The message sent when attempting to update an island's hopper limit with a negative amount.
 * @param islandNotFound The message sent when an island was not found for a player while attempting to update another player's island's hopper limit.
 */
@ConfigSerializable
public record Locale(
        String configVersion,
        String prefix,
        String reload,
        @NotNull List<String> help,
        String playerOnly,
        String notOnIsland,
        String islandMemberOrOwnerOnly,
        String guiOpenError,
        String insufficientFunds,
        String hopperLimitUpgraded,
        String hopperLimitUpdated,
        String playerHopperLimitUpdated,
        String playerHopperLimitOffset,
        String amountMustBePositive,
        String islandNotFound) {}
