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
package com.github.lukesky19.hopperlimitupgrades.integration;

import com.github.lukesky19.hopperlimitupgrades.integration.hooks.BentoBoxHook;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.EconomyHook;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.LimitsAddonHook;
import com.github.lukesky19.hopperlimitupgrades.integration.hooks.PlayerPointsHook;
import com.github.lukesky19.skylib.common.api.integration.Hook;
import com.github.lukesky19.skylib.paper.api.plugin.SkyPlugin;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages hooks into different plugins.
 */
public class HookManager {
    private final @NonNull Map<Class<?>, Hook> hooks = new HashMap<>();

    /**
     * Constructor
     * @param plugin A {@link SkyPlugin} instance.
     */
    public HookManager(@NonNull SkyPlugin plugin) {
        registerHook(BentoBoxHook.class, new BentoBoxHook());

        registerHook(LimitsAddonHook.class, new LimitsAddonHook(plugin));

        registerHook(EconomyHook.class, new EconomyHook(plugin));

        registerHook(PlayerPointsHook.class, new PlayerPointsHook(plugin));
    }

    /**
     * Register a hook.
     * @param hookClass The class.
     * @param hook The class instance.
     * @param <T> Parameter for any class that extends {@link Hook}.
     */
    public <T extends Hook> void registerHook(@NonNull Class<T> hookClass, @NonNull Hook hook) {
        hooks.put(hookClass, hook);
        hook.initialize();
    }

    /**
     * Get a hook.
     * @param hookClass The class.
     * @param <T> Parameter for any class that extends {@link Hook}.
     * @return The class instance.
     */
    public @NonNull <T extends Hook> T getHook(@NonNull Class<T> hookClass) {
        return hookClass.cast(hooks.get(hookClass));
    }
}