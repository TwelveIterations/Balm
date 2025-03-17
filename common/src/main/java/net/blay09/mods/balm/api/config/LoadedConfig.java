package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public interface LoadedConfig {
    <T> T getRaw(ConfiguredProperty<T> property);

    MutableLoadedConfig mutable(BalmConfigSchema schema);
}
