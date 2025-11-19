package net.blay09.mods.balm.platform.config;

import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;

public interface LoadedConfig {
    <T> T getRaw(ConfiguredProperty<T> property);

    MutableLoadedConfig mutable(BalmConfigSchema schema);
}
