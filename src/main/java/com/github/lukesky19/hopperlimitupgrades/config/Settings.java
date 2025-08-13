package com.github.lukesky19.hopperlimitupgrades.config;

import com.github.lukesky19.skylib.libs.configurate.objectmapping.ConfigSerializable;
import org.jetbrains.annotations.Nullable;

/**
 * This record contains the plugin's settings.
 * @param configVersion The config version of the plugin's settings.
 * @param locale THe locale to use.
 */
@ConfigSerializable
public record Settings(@Nullable String configVersion, @Nullable String locale) { }
