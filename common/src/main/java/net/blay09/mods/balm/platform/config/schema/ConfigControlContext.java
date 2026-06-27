package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record ConfigControlContext<T>(
        ConfiguredProperty<T> property,
        Supplier<T> getter,
        Consumer<T> setter,
        Component displayName,
        Component tooltip) {

    public ConfigControlContext(ConfiguredProperty<T> property, MutableLoadedConfig config, Component displayName, Component tooltip) {
        this(property, () -> property.getRaw(config), value -> property.setRaw(config, property.validateValue(value).getOrThrow()), displayName, tooltip);
    }

    public T get() {
        return getter.get();
    }

    public void set(T value) {
        setter.accept(property.validateValue(value).getOrThrow());
    }
}
