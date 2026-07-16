package net.blay09.mods.balm.platform.config.schema;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigControlBinding<T> {
    private final ConfiguredProperty<T> property;
    private final Supplier<T> getter;
    private final Consumer<T> setter;

    public ConfigControlBinding(
            ConfiguredProperty<T> property,
            Supplier<T> getter,
            Consumer<T> setter) {
        this.property = property;
        this.getter = getter;
        this.setter = setter;
    }

    public ConfigControlBinding(ConfiguredProperty<T> property, MutableLoadedConfig config) {
        this(property, () -> property.getRaw(config), value -> property.setRaw(config, property.validateValue(value).getOrThrow()));
    }

    public T get() {
        return getter.get();
    }

    public void set(T value) {
        setter.accept(property.validateValue(value).getOrThrow());
    }

    public Supplier<T> getter() {
        return getter;
    }

    public Consumer<T> setter() {
        return setter;
    }

    public T defaultValue() {
        return property.defaultValue();
    }

    public DataResult<T> validateValue(T value) {
        return property.validateValue(value);
    }
}
