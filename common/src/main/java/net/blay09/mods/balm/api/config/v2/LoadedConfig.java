package net.blay09.mods.balm.api.config.v2;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public interface LoadedConfig {
    <T> T getRaw(ConfiguredProperty<T> property);
}
