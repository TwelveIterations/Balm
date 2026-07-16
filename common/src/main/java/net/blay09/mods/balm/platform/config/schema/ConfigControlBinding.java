package net.blay09.mods.balm.platform.config.schema;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.minecraft.network.chat.Component;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigControlBinding<T> {
    private final ConfiguredProperty<T> property;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Component displayName;
    private final Component tooltip;

    public ConfigControlBinding(
            ConfiguredProperty<T> property,
            Supplier<T> getter,
            Consumer<T> setter,
            Component displayName,
            Component tooltip) {
        this.property = property;
        this.getter = getter;
        this.setter = setter;
        this.displayName = displayName;
        this.tooltip = tooltip;
    }

    public ConfigControlBinding(ConfiguredProperty<T> property, MutableLoadedConfig config, Component displayName, Component tooltip) {
        this(property, () -> property.getRaw(config), value -> property.setRaw(config, property.validateValue(value).getOrThrow()), displayName, tooltip);
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

    public Component displayName() {
        return displayName;
    }

    public Component tooltip() {
        return tooltip;
    }

    public T defaultValue() {
        return property.defaultValue();
    }

    public DataResult<T> validateValue(T value) {
        return property.validateValue(value);
    }
}
