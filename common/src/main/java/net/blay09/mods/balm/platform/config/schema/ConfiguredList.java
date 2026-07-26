package net.blay09.mods.balm.platform.config.schema;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

import java.util.List;

public interface ConfiguredList<T> extends ConfiguredProperty<List<T>>, NestedTypeHolder<T> {
    boolean hasCustomCollectionValidator();

    DataResult<T> validateElement(T value);

    default List<T> get(LoadedConfig config) {
        return getRaw(config);
    }

    default List<T> get() {
        final var config = Balm.config().getActiveConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No active config found for schema " + parentSchema().identifier());
        }
        return get(config);
    }

    default void set(MutableLoadedConfig config, List<T> value) {
        setRaw(config, value);
    }

    default void set(List<T> value) {

        final var config = Balm.config().getLocalConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No local config loaded for schema " + parentSchema().identifier());
        }
        set(config, value);
    }
}
