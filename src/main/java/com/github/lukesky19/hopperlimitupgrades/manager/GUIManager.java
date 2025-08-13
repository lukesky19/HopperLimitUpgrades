package com.github.lukesky19.hopperlimitupgrades.manager;

import com.github.lukesky19.hopperlimitupgrades.HopperLimitUpgrades;
import com.github.lukesky19.skylib.api.gui.AbstractGUIManager;
import org.jetbrains.annotations.NotNull;

/**
 * This class manages open GUIs.
 */
public class GUIManager extends AbstractGUIManager {
    /**
     * Constructor
     * @param hopperLimitUpgrades A {@link HopperLimitUpgrades} instance.
     */
    public GUIManager(@NotNull HopperLimitUpgrades hopperLimitUpgrades) {
        super(hopperLimitUpgrades);
    }
}
