package net.blay09.mods.balm.api.config.v2;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public interface LoadedBalmConfig {
    <T> void setRaw(ConfiguredProperty<T> property, T value);

    <T> T getRaw(ConfiguredProperty<T> property);
}
