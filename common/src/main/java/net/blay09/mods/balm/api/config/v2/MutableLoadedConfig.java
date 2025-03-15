package net.blay09.mods.balm.api.config.v2;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public interface MutableLoadedConfig extends LoadedConfig {
    <T> void setRaw(ConfiguredProperty<T> property, T value);
}
