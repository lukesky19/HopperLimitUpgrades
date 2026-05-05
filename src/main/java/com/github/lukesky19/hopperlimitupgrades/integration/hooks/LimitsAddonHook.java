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
package com.github.lukesky19.hopperlimitupgrades.integration.hooks;

import com.github.lukesky19.skylib.common.api.integration.Hook;
import com.github.lukesky19.skylib.paper.api.plugin.SkyPlugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.limits.Limits;
import world.bentobox.limits.listeners.BlockLimitsListener;
import world.bentobox.limits.objects.IslandBlockCount;

import java.util.Optional;

/**
 * This class manages interfacing with the limits addon from BentoBox.
 */
public class LimitsAddonHook implements Hook {
    private final @NonNull SkyPlugin skyPlugin;
    private @Nullable BlockLimitsListener blockLimitsListener;

    /**
     * Constructor
     * @param skyPlugin A {@link SkyPlugin} instance.
     */
    public LimitsAddonHook(@NonNull SkyPlugin skyPlugin) {
        this.skyPlugin = skyPlugin;
    }

    /**
     * Get the {@link Limits} instance and any other classes necessary.
     */
    @Override
    public void initialize() {
        if(!skyPlugin.getServer().getPluginManager().isPluginEnabled("BentoBox")) return;

        Optional<Addon> optionalAddon = BentoBox.getInstance().getAddonsManager().getAddonByName("Limits");
        if(optionalAddon.isEmpty()) return;

        Limits limits = (Limits) optionalAddon.get();
        blockLimitsListener = limits.getBlockLimitListener();
    }

    /**
     * Is the hook initialized?
     * @return true if hooked, otherwise false.
     */
    @Override
    public boolean isHooked() {
        return blockLimitsListener != null;
    }

    /**
     * Get the {@link IslandBlockCount} for the island.
     * @param island The {@link Island}.
     * @return The {@link IslandBlockCount} or null.
     */
    public @Nullable IslandBlockCount getIslandBlockCount(@NonNull Island island) {
        if(blockLimitsListener == null) initialize();
        if(blockLimitsListener == null) return null;

        return blockLimitsListener.getIsland(island);
    }
}