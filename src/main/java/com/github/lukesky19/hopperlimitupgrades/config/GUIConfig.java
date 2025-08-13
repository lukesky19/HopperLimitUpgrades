package com.github.lukesky19.hopperlimitupgrades.config;

import com.github.lukesky19.skylib.api.gui.GUIType;
import com.github.lukesky19.skylib.api.itemstack.ItemStackConfig;
import com.github.lukesky19.skylib.libs.configurate.objectmapping.ConfigSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This record contains the configuration for the upgrade GUI.
 * @param configVersion The config version.
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
        @Nullable String configVersion,
        @Nullable String guiName,
        @Nullable GUIType guiType,
        @NotNull ItemStackConfig filler,
        @NotNull ButtonConfig nextPage,
        @NotNull ButtonConfig prevPage,
        @NotNull ButtonConfig exit,
        @NotNull List<ButtonConfig> dummyButtons,
        @NotNull List<UpgradeButtonConfig> upgradeButtons) {
    /**
     * This record contains the configuration for a single button to display in a GUI.
     * @param item The {@link ItemStackConfig} for the {@link ItemStack}.
     * @param slot The slot to place the {@link ItemStack} at.
     */
    @ConfigSerializable
    public record ButtonConfig(
            @NotNull ItemStackConfig item,
            @Nullable Integer slot) {}

    /**
     * This record contains the configuration for a single upgrade button to display in a GUI.
     * @param purchasableItem The {@link ItemStackConfig} for the {@link ItemStack} to show when the upgrade is purchasable.
     * @param purchasedItem The {@link ItemStackConfig} for the {@link ItemStack} to show when the upgrade is already purchased.
     * @param slot The slot to place the {@link ItemStack} at.
     * @param offsetAmount The offset amount for the upgrade.
     * @param price The price to purchase the upgrade.
     */
    @ConfigSerializable
    public record UpgradeButtonConfig(
            @NotNull ItemStackConfig purchasableItem,
            @NotNull ItemStackConfig purchasedItem,
            @Nullable Integer slot,
            @Nullable Integer offsetAmount,
            @Nullable Double price) {}
}
