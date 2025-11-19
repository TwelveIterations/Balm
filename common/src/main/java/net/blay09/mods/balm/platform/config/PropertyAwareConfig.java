package net.blay09.mods.balm.platform.config;

import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;

public interface PropertyAwareConfig {
    boolean hasProperty(ConfiguredProperty<?> property);
}
