package net.blay09.mods.balm.client.platform.config.internal;

import net.blay09.mods.balm.client.platform.config.ConfigControl;
import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ConfigControlRegistry {
    private static final Map<Identifier, ConfigControl<?>> controls = new ConcurrentHashMap<>();

    private ConfigControlRegistry() {
    }

    public static <T> void register(Identifier identifier, ConfigControl<T> control) {
        controls.put(identifier, control);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<ConfigControl<T>> get(Identifier identifier) {
        return Optional.ofNullable((ConfigControl<T>) controls.get(identifier));
    }

    public static <T> Optional<Object> createElement(Identifier identifier, ConfigControlBinding<T> binding, ConfigControlContext context) {
        return ConfigControlRegistry.<T>get(identifier).flatMap(control -> control.createElement(binding, context));
    }
}
