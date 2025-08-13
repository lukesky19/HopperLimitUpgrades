package com.github.lukesky19.hopperlimitupgrades.gui;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.hopperlimitupgrades.config.GUIConfig;
import com.github.lukesky19.hopperlimitupgrades.config.Locale;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIConfigManager;
import com.github.lukesky19.hopperlimitupgrades.manager.GUIManager;
import com.github.lukesky19.hopperlimitupgrades.manager.LocaleManager;
import com.github.lukesky19.skylib.api.adventure.AdventureUtil;
import com.github.lukesky19.skylib.api.gui.GUIButton;
import com.github.lukesky19.skylib.api.gui.GUIType;
import com.github.lukesky19.skylib.api.gui.abstracts.ChestGUI;
import com.github.lukesky19.skylib.api.itemstack.ItemStackBuilder;
import com.github.lukesky19.skylib.api.itemstack.ItemStackConfig;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.limits.Limits;
import world.bentobox.limits.listeners.BlockLimitsListener;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.*;

/**
 * Creates the GUI to upgrade an island's hopper limit.
 */
public class UpgradeGUI extends ChestGUI {
    private final @NotNull HopperLimitUpgrades hopperLimitUpgrades;
    private final @NotNull LocaleManager localeManager;
    private final @NotNull Island island;
    private final @Nullable GUIConfig guiConfig;

    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     * @param guiConfigManager A {@link GUIConfigManager} instance.
     * @param guiManager A {@link GUIManager} instance.
     * @param localeManager A {@link LocaleManager} instance.
     * @param player The {@link Player} viewing the GUI.
     * @param island The {@link Island} to apply hopper limit offsets to.
     */
    public UpgradeGUI(
            @NotNull HopperLimitUpgrades hopperLimitUpgrades,
            @NotNull GUIConfigManager guiConfigManager,
            @NotNull GUIManager guiManager,
            @NotNull LocaleManager localeManager,
            @NotNull Player player,
            @NotNull Island island) {
        super(hopperLimitUpgrades, guiManager, player);

        this.hopperLimitUpgrades = hopperLimitUpgrades;
        this.localeManager = localeManager;

        this.island = island;
        this.guiConfig = guiConfigManager.getGuiConfig();
    }

    /**
     * Create the {@link InventoryView} for this GUI.
     * @return true if created successfully, otherwise false.
     */
    public boolean create() {
        if(guiConfig == null) {
            logger.warn(AdventureUtil.serialize("Unable to create the InventoryView for the upgrade GUI due to invalid gui configuration."));
            return false;
        }

        GUIType guiType = guiConfig.guiType();
        if(guiType == null) {
            logger.warn(AdventureUtil.serialize("Unable to create the InventoryView for the upgrade GUI due to an invalid GUIType."));
            return false;
        }

        String guiName = Objects.requireNonNullElse(guiConfig.guiName(), "");

        return create(guiType, guiName, List.of());
    }

    /**
     * Create all the buttons and decorate the GUI.
     * @return true if updated successfully, otherwise false.
     */
    @Override
    public boolean update() {
        clearButtons();

        if(guiConfig == null) {
            logger.warn(AdventureUtil.serialize("Unable to add buttons to the GUI as the gui configuration is invalid."));
            return false;
        }

        // If the InventoryView was not created, log a warning and return false.
        if(inventoryView == null) {
            logger.warn(AdventureUtil.serialize("Unable to add buttons to the GUI as the InventoryView was not created."));
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
    public void handleClose(@NotNull InventoryCloseEvent inventoryCloseEvent) {
        if(inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.UNLOADED) || inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) return;

        guiManager.removeOpenGUI(uuid);
    }

    /**
     * This method does nothing.
     * @param inventoryDragEvent An {@link InventoryDragEvent}
     */
    @Override
    public void handleBottomDrag(@NotNull InventoryDragEvent inventoryDragEvent) {}

    /**
     * This method does nothing.
     * @param inventoryDragEvent An {@link InventoryDragEvent}
     */
    @Override
    public void handleGlobalDrag(@NotNull InventoryDragEvent inventoryDragEvent) {}

    /**
     * This method does nothing.
     * @param inventoryClickEvent An {@link InventoryClickEvent}
     */
    @Override
    public void handleBottomClick(@NotNull InventoryClickEvent inventoryClickEvent) {}

    /**
     * This method does nothing.
     * @param inventoryClickEvent An {@link InventoryClickEvent}
     */
    @Override
    public void handleGlobalClick(@NotNull InventoryClickEvent inventoryClickEvent) {}

    /**
     * Create the filler buttons for the GUI.
     * @param guiSize The size of the GUI.
     */
    private void createFillerButtons(int guiSize) {
        if(guiConfig == null) return;

        ItemStackConfig fillerConfig = guiConfig.filler();
        ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
        itemStackBuilder.fromItemStackConfig(fillerConfig, player, null, List.of());

        Optional<@NotNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
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
                logger.warn(AdventureUtil.serialize("Unable to add a dummy button to the upgrade GUI due to an invalid slot."));
                return;
            }

            ItemStackConfig itemStackConfig = buttonConfig.item();
            ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
            itemStackBuilder.fromItemStackConfig(itemStackConfig, player, null, List.of());
            Optional<@NotNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
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
            logger.warn(AdventureUtil.serialize("Unable to add a exit button due to a slot not being configured."));
            return;
        }

        // Get the ItemStackConfig
        ItemStackConfig itemConfig = guiConfig.exit().item();

        // Create the ItemStackBuilder and pass the ItemStackConfig.
        ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
        itemStackBuilder.fromItemStackConfig(itemConfig, player, null, List.of());

        // If an ItemStack was created, create the GUIButton and add it to the GUI.
        Optional<ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
        optionalItemStack.ifPresent(itemStack -> {
            GUIButton.Builder guiButtonBuilder = new GUIButton.Builder();
            guiButtonBuilder.setItemStack(itemStack);
            guiButtonBuilder.setAction(event -> close());

            setButton(guiConfig.exit().slot(), guiButtonBuilder.build());
        });
    }

    /**
     * Create the upgrade buttons for the GUI.
     */
    private void createUpgrades() {
        @NotNull Locale locale = localeManager.getLocale();
        if(guiConfig == null) return;
        Limits limitsAddon = hopperLimitUpgrades.getLimitsAddon();
        BlockLimitsListener blockLimitListener = limitsAddon.getBlockLimitListener();

        IslandBlockCount islandBlockCount = blockLimitListener.getIsland(island);
        int hopperLimitOffset = islandBlockCount.getBlockLimitOffset(Material.HOPPER);

        for(GUIConfig.UpgradeButtonConfig upgradeButtonConfig : guiConfig.upgradeButtons()) {
            if(upgradeButtonConfig.slot() == null) {
                logger.warn(AdventureUtil.serialize("Unable to add a upgrade button to the upgrade GUI due to an invalid slot."));
                continue;
            }

            if(upgradeButtonConfig.price() == null || upgradeButtonConfig.price() <= 0) {
                logger.warn(AdventureUtil.serialize("Unable to add a upgrade button to the upgrade GUI due to an invalid price."));
                continue;
            }

            if(upgradeButtonConfig.offsetAmount() == null || upgradeButtonConfig.offsetAmount() <= 0) {
                logger.warn(AdventureUtil.serialize("Unable to add a upgrade button to the upgrade GUI due to an invalid offset amount."));
                continue;
            }

            if(hopperLimitOffset < upgradeButtonConfig.offsetAmount()) {
                ItemStackConfig itemStackConfig = upgradeButtonConfig.purchasableItem();
                ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
                itemStackBuilder.fromItemStackConfig(itemStackConfig, player, null, List.of());
                Optional<@NotNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
                if(optionalItemStack.isPresent()) {
                    GUIButton.Builder upgradeBuilder = new GUIButton.Builder();
                    upgradeBuilder.setItemStack(optionalItemStack.get());
                    upgradeBuilder.setAction(inventoryClickEvent -> {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        Economy economy = hopperLimitUpgrades.getEconomy();

                        if(economy.getBalance(player) >= upgradeButtonConfig.price()) {
                            economy.withdrawPlayer(player, upgradeButtonConfig.price());
                            islandBlockCount.setBlockLimitsOffset(Material.HOPPER, upgradeButtonConfig.offsetAmount());

                            int updatedAmount = islandBlockCount.getBlockLimit(Material.HOPPER) + islandBlockCount.getBlockLimitOffset(Material.HOPPER);
                            List<TagResolver.Single> placeholders = List.of(Placeholder.parsed("amount", String.valueOf(updatedAmount)));

                            player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.hopperLimitUpgraded(), placeholders));

                            update();
                        } else {
                            player.sendMessage(AdventureUtil.serialize(locale.prefix() + locale.insufficientFunds()));
                        }
                    });

                    setButton(upgradeButtonConfig.slot(), upgradeBuilder.build());
                }
            } else {
                ItemStackConfig itemStackConfig = upgradeButtonConfig.purchasedItem();
                ItemStackBuilder itemStackBuilder = new ItemStackBuilder(logger);
                itemStackBuilder.fromItemStackConfig(itemStackConfig, player, null, List.of());
                Optional<@NotNull ItemStack> optionalItemStack = itemStackBuilder.buildItemStack();
                if(optionalItemStack.isPresent()) {
                    GUIButton.Builder upgradeBuilder = new GUIButton.Builder();
                    upgradeBuilder.setItemStack(optionalItemStack.get());

                    setButton(upgradeButtonConfig.slot(), upgradeBuilder.build());
                }
            }
        }
    }
}
