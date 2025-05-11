package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface PropertyAwareConfig {
    boolean hasProperty(ConfiguredProperty<?> property);
}
