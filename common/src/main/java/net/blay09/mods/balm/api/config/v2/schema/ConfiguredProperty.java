package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.config.v2.LoadedBalmConfig;
import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigSchemaImpl;

public interface ConfiguredProperty<T> {
    ConfigSchemaImpl parentSchema();
    String category();
    String name();
    String comment();
    Class<?> type();
    T defaultValue();
    default T getRaw(LoadedBalmConfig config) {
        return config.getRaw(this);
    }
    default void setRaw(LoadedBalmConfig config, T value) {
        config.setRaw(this, value);
    }
}
