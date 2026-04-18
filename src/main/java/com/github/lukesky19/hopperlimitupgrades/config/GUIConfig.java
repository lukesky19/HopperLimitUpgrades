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
import com.github.lukesky19.skylib.paper.api.gui.GUIType;
import com.github.lukesky19.skylib.paper.api.itemstack.ItemStackConfig;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * This record contains the configuration for the upgrade GUI.
 * @param version The config version.
 * @param guiName The name to use in the GUI.
 * @param guiType The {@link GUIType}.
 * @param filler The {@link ItemStackConfig} to fill the GUI with.
 * @param nextPage The {@link ButtonConfig} for the next page button.
 * @param prevPage The {@link ButtonConfig} for the previous page button.
 * @param exit The {@link ButtonConfig} for the exit button.
 * @param dummyButtons A {@link List} of {@link ButtonConfig}s to display in the GUI.
 * @param upgradeButtons A {@link List} of {@link UpgradeButtonConfig}s to display in the GUI.
 */
@ConfigSerializable
public record GUIConfig(
        int version,
        @Nullable String guiName,
        @Nullable GUIType guiType,
        @NonNull ItemStackConfig filler,
        @NonNull ButtonConfig nextPage,
        @NonNull ButtonConfig prevPage,
        @NonNull ButtonConfig exit,
        @NonNull List<ButtonConfig> dummyButtons,
        @NonNull List<UpgradeButtonConfig> upgradeButtons) {
    /**
     * This record contains the configuration for a single button to display in a GUI.
     * @param item The {@link ItemStackConfig} for the {@link ItemStack}.
     * @param slot The slot to place the {@link ItemStack} at.
     */
    @ConfigSerializable
    public record ButtonConfig(
            @NonNull ItemStackConfig item,
            @Nullable Integer slot) {}

    /**
     * This record contains the configuration for a single upgrade button to display in a GUI.
     * @param purchasableItem The {@link ItemStackConfig} for the {@link ItemStack} to show when the upgrade is purchasable.
     * @param purchasedItem The {@link ItemStackConfig} for the {@link ItemStack} to show when the upgrade is already purchased.
     * @param slot The slot to place the {@link ItemStack} at.
     * @param offsetAmount The offset amount for the upgrade.
     * @param prices The prices to purchase the upgrade.
     * @param price The legacy money price to purchase the upgrade. For migration purposes only.
     */
    @ConfigSerializable
    public record UpgradeButtonConfig(
            @NonNull ItemStackConfig purchasableItem,
            @NonNull ItemStackConfig purchasedItem,
            @Nullable Integer slot,
            @Nullable Integer offsetAmount,
            @NonNull PriceConfig prices,
            @Deprecated(since = "1.2.0.0") @Nullable Double price) {}

    /**
     * This record contains the configuration for the prices for an upgrade.
     * @param money The money required.
     * @param playerPoints The player points required.
     */
    @ConfigSerializable
    public record PriceConfig(
            @Nullable Double money,
            @Nullable Integer playerPoints) {}
}