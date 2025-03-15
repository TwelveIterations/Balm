package net.blay09.mods.balm.api.config.v2.reflection;

import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public class LoadedReflectionConfig implements MutableLoadedConfig {
    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
// TODO
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        return null;
    }
}
