package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigSchemaImpl;

public interface ConfiguredProperty<T> {
    ConfigSchemaImpl parentSchema();
    String category();
    String name();
    String comment();
    Class<?> type();
    T defaultValue();
    default T getRaw(LoadedConfig config) {
        return config.getRaw(this);
    }
    default void setRaw(MutableLoadedConfig config, T value) {
        config.setRaw(this, value);
    }
}
