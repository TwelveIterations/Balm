package net.blay09.mods.balm.api.config.v2.reflection;

import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public class LoadedReflectionConfig<ConfigData> implements MutableLoadedConfig {

    private final ConfigData configData;

    public LoadedReflectionConfig(ConfigData configData) {
        this.configData = configData;
    }

    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
// TODO
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        return null;
    }
}
