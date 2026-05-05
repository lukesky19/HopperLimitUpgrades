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
package com.github.lukesky19.hopperlimitupgrades.gui;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.config.GUIConfig;
import com.github.lukesky19.hopperlimitupgrades.config.Locale;
import com.github.lukesky19.hopperlimitupgrades.integration.HookManager;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.EconomyHook;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.LimitsAddonHook;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.PlayerPointsHook;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIConfigManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LocaleManager;
import com.github.lukesky19.skylib.common.api.adventure.AdventureUtility;
import com.github.lukesky19.skylib.paper.api.gui.GUIButton;
import com.github.lukesky19.skylib.paper.api.gui.GUIType;
import com.github.lukesky19.skylib.paper.api.gui.impl.UUIDGUIManager;
import com.github.lukesky19.skylib.paper.api.gui.templates.ChestGUI;
import com.github.lukesky19.skylib.paper.api.itemstack.ItemStackBuilder;
import com.github.lukesky19.skylib.paper.api.itemstack.ItemStackConfig;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Creates the GUI to upgrade an island's hopper limit.
 */
public class UpgradeGUI extends ChestGUI<UUID> {
    private final @NonNull LocaleManager localeManager;
    private final @NonNull HookManager hookManager;
    private final @NonNull Island island;
    private final @Nullable GUIConfig guiConfig;

    private final World.@NonNull Environment environment;
    private final @NonNull String environmentName;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     * @param guiConfigManager A {@link GUIConfigManager} instance.
     * @param guiManager A {@link UUIDGUIManager} instance.
     * @param localeManager A {@link LocaleManager} instance.
     * @param hookManager A {@link HookManager} instance.
     * @param player The {@link Player} viewing the GUI.
     * @param island The {@link Island} to apply hopper limit offsets to.
     * @param environment The {@link World.Environment} to apply limits to.
     */
    public UpgradeGUI(
            @NonNull HopperLimitUpgrades hopperLimitUpgrades,
            @NonNull GUIConfigManager guiConfigManager,
            @NonNull UUIDGUIManager guiManager,
            @NonNull LocaleManager localeManager,
            @NonNull HookManager hookManager,
            @NonNull Player player,
            @NonNull Island island,
            World.@NonNull Environment environment) {
        super(hopperLimitUpgrades, guiManager, player.getUniqueId(), player);

        this.localeManager = localeManager;
        this.hookManager = hookManager;

        this.island = island;
        this.guiConfig = guiConfigManager.getConfiguration();
        this.environment = environment;

        environmentName = switch(environment) {
            case NORMAL -> "Overworld";
            case NETHER -> "Nether";
            case THE_END -> "End";
            case CUSTOM -> throw new RuntimeException("Unsupported environment.");
        };
    }

    /**
     * Create the {@link InventoryView} for this GUI.
     * @return true if created successfully, otherwise false.
     */
    public boolean create() {
        if(guiConfig == null) {
            logger.warn(AdventureUtility.deserialize("Unable to create the InventoryView for the upgrade GUI due to invalid gui configuration."));
            return false;
        }

        GUIType guiType = guiConfig.guiType();
        if(guiType == null) {
            logger.warn(AdventureUtility.deserialize("Unable to create the InventoryView for the upgrade GUI due to an invalid GUIType."));
            return false;
        }

        String guiName = Objects.requireNonNullElse(guiConfig.guiName(), "");

        return create(guiType, guiName, List.of(Placeholder.parsed("dimension", environmentName)));
    }

    /**
     * Create all the buttons and decorate the GUI.
     * @return true if updated successfully, otherwise false.
     */
    @Override
    public boolean update() {
        clearButtons();

        if(guiConfig == null) {
            logger.warn(AdventureUtility.deserialize("Unable to add buttons to the GUI as the gui configuration is invalid."));
            return false;
        }

        // If the InventoryView was not created, log a warning and return false.
        if(inventoryView == null) {
            logger.warn(AdventureUtility.deserialize("Unable to add buttons to the GUI as the InventoryView was not created."));
            return false;
        }

        int guiSize = inventoryView.getTopInventory().getSize();

        createFillerButtons(guiSize);
        createDummyButtons();
        createExitButton();
        createUpgrades();

        return super.update();
    }

    /**
     * Handles when the GUI is closed by the player.
     * @param inventoryCloseEvent An {@link InventoryCloseEvent}
     */
    @Override
    public void handleClose(@NonNull InventoryCloseEvent inventoryCloseEvent) {
        if(inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.UNLOADED) || inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) return;

        guiManager.removeOpenGUI(uuid);
    }

    /**
     * This method does nothing.
     * @param inventoryDragEvent An {@link InventoryDragEvent}
     */
    @Override
    public void handleBottomDrag(@NonNull InventoryDragEvent inventoryDragEvent) {}

    /**
     * This method does nothing.
     * @param inventoryDragEvent An {@link InventoryDragEvent}
     */
    @Override
    public void handleGlobalDrag(@NonNull InventoryDragEvent inventoryDragEvent) {}

    /**
     * This method does nothing.
     * @param inventoryClickEvent An {@link InventoryClickEvent}
     */
    @Override
    public void handleBottomClick(@NonNull InventoryClickEvent inventoryClickEvent) {}

    /**
     * This method does nothing.
     * @param inventoryClickEvent An {@link InventoryClickEvent}
     */
    @Override
    public void handleGlobalClick(@NonNull InventoryClickEvent inventoryClickEvent) {}

    /**
     * Create the filler buttons for the GUI.
     * @param guiSize The size of the GUI.
     */
    private void createFillerButtons(int guiSize) {
        if(guiConfig == null) return;

        ItemStackConfig fillerConfig = guiConfig.filler();
        ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
        itemStackBuilder.fromItemStackConfig(fillerConfig, player, List.of());

        Optional<@NonNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
        optionalItemStack.ifPresent(itemStack -> {
            GUIButton.Builder builder = new GUIButton.Builder();
            builder.setItemStack(itemStack);

            for(int i = 0; i <= guiSize - 1; i++) {
                setButton(i, builder.build());
            }
        });
    }

    /**
     * Create the dummy buttons for the GUI.
     */
    private void createDummyButtons() {
        if(guiConfig == null) return;

        guiConfig.dummyButtons().forEach(buttonConfig -> {
            if(buttonConfig.slot() == null) {
                logger.warn(AdventureUtility.deserialize("Unable to add a dummy button to the upgrade GUI due to an invalid slot."));
                return;
            }

            ItemStackConfig itemStackConfig = buttonConfig.item();
            ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
            itemStackBuilder.fromItemStackConfig(itemStackConfig, player, List.of());
            Optional<@NonNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
            optionalItemStack.ifPresent(itemStack -> {
                GUIButton.Builder builder = new GUIButton.Builder();

                builder.setItemStack(itemStack);

                setButton(buttonConfig.slot(), builder.build());
            });
        });
    }

    /**
     * Create the button to exit the GUI.
     */
    private void createExitButton() {
        if(guiConfig == null) return;

        // Check if the slot is not configured and send a warning.
        if(guiConfig.exit().slot() == null) {
            logger.warn(AdventureUtility.deserialize("Unable to add a exit button due to a slot not being configured."));
            return;
        }

        // Get the ItemStackConfig
        ItemStackConfig itemConfig = guiConfig.exit().item();

        // Create the ItemStackBuilder and pass the ItemStackConfig.
        ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
        itemStackBuilder.fromItemStackConfig(itemConfig, player, List.of());

        // If an ItemStack was created, create the GUIButton and add it to the GUI.
        Optional<ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
        optionalItemStack.ifPresent(itemStack -> {
            GUIButton.Builder guiButtonBuilder = new GUIButton.Builder();
            guiButtonBuilder.setItemStack(itemStack);
            guiButtonBuilder.setAction(_ -> close());

            setButton(guiConfig.exit().slot(), guiButtonBuilder.build());
        });
    }

    /**
     * Create the upgrade buttons for the GUI.
     */
    private void createUpgrades() {
        Locale locale = localeManager.getConfiguration();
        if(guiConfig == null) return;
        LimitsAddonHook limitsAddon = hookManager.getHook(LimitsAddonHook.class);

        IslandBlockCount islandBlockCount = limitsAddon.getIslandBlockCount(island);
        if(islandBlockCount == null) return;
        int hopperLimitOffset = islandBlockCount.getBlockLimitOffset(environment, Material.HOPPER.getKey());

        for(GUIConfig.UpgradeButtonConfig buttonConfig : guiConfig.upgradeButtons()) {
            if(buttonConfig.slot() == null) {
                logger.warn(AdventureUtility.deserialize("Unable to add a upgrade button to the upgrade GUI due to an invalid slot."));
                continue;
            }

            GUIConfig.PriceConfig priceConfig = buttonConfig.prices();
            boolean hasMoney = priceConfig.money() != null && priceConfig.money() > 0;
            boolean hasPoints = priceConfig.playerPoints() != null && priceConfig.playerPoints() > 0;
            if(!hasMoney && !hasPoints) {
                logger.warn(AdventureUtility.deserialize("Unable to add a upgrade button to the upgrade GUI due to an invalid price configuration."));
                continue;
            }

            if(buttonConfig.offsetAmount() == null || buttonConfig.offsetAmount() <= 0) {
                logger.warn(AdventureUtility.deserialize("Unable to add a upgrade button to the upgrade GUI due to an invalid offset amount."));
                continue;
            }

            if(hopperLimitOffset < buttonConfig.offsetAmount()) {
                ItemStackConfig itemStackConfig = buttonConfig.purchasableItem();
                ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
                itemStackBuilder.fromItemStackConfig(itemStackConfig, player, List.of());

                Optional<@NonNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
                if(optionalItemStack.isPresent()) {
                    GUIButton.Builder upgradeBuilder = new GUIButton.Builder();
                    upgradeBuilder.setItemStack(optionalItemStack.get());

                    upgradeBuilder.setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        EconomyHook economyHook = hookManager.getHook(EconomyHook.class);
                        PlayerPointsHook playerPointsHook = hookManager.getHook(PlayerPointsHook.class);

                        if(hasMoney) {
                            if(economyHook.isHooked()) {
                                if(economyHook.getBalance(player) < priceConfig.money()) {
                                    player.sendMessage(AdventureUtility.deserialize(locale.prefix() + locale.insufficientMoney()));
                                    return;
                                }
                            } else {
                                logger.warn(AdventureUtility.plain("An upgrade button has money configured, but no economy is hooked into."));
                                player.sendMessage(AdventureUtility.deserialize(locale.prefix() + locale.upgradePriceError()));
                                return;
                            }
                        }

                        if(hasPoints) {
                            if(playerPointsHook.isHooked()) {
                                if(playerPointsHook.getBalance(player) < priceConfig.playerPoints()) {
                                    player.sendMessage(AdventureUtility.deserialize(locale.prefix() + locale.insufficientPlayerPoints()));
                                    return;
                                }
                            } else {
                                logger.warn(AdventureUtility.plain("An upgrade button has money configured, but no economy is hooked into."));
                                player.sendMessage(AdventureUtility.deserialize(locale.prefix() + locale.upgradePriceError()));
                                return;
                            }
                        }

                        // Remove appropriate prices from player
                        if(hasMoney && economyHook.isHooked()) {
                            economyHook.removeFromBalance(player, priceConfig.money());
                        }
                        if(hasPoints && playerPointsHook.isHooked()) {
                            playerPointsHook.removeFromBalance(player, priceConfig.playerPoints());
                        }

                        // Update limit
                        islandBlockCount.setBlockLimitsOffset(environment, Material.HOPPER.getKey(), buttonConfig.offsetAmount());

                        // Create placeholders for confirmation message
                        int updatedAmount = islandBlockCount.getBlockLimit(environment, Material.HOPPER.getKey()) + islandBlockCount.getBlockLimitOffset(environment, Material.HOPPER.getKey());
                        List<TagResolver.Single> placeholders = List.of(
                                Placeholder.parsed("amount", String.valueOf(updatedAmount)),
                                Placeholder.parsed("dimension", environmentName.toLowerCase()));

                        // Send player confirmation message
                        player.sendMessage(AdventureUtility.deserialize(locale.prefix() + locale.hopperLimitUpgraded(), placeholders));

                        // Refresh the GUI
                        update();
                    });

                    setButton(buttonConfig.slot(), upgradeBuilder.build());
                }
            } else {
                ItemStackConfig itemStackConfig = buttonConfig.purchasedItem();
                ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
                itemStackBuilder.fromItemStackConfig(itemStackConfig, player, List.of());
                Optional<@NonNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
                if(optionalItemStack.isPresent()) {
                    GUIButton.Builder upgradeBuilder = new GUIButton.Builder();
                    upgradeBuilder.setItemStack(optionalItemStack.get());

                    setButton(buttonConfig.slot(), upgradeBuilder.build());
                }
            }
        }
    }
}