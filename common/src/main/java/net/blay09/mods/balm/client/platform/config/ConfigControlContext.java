package net.blay09.mods.balm.client.platform.config;

import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.network.chat.Component;

public interface ConfigControlContext {

    int entryWidth();

    int entryHeight();

    Component label(ConfiguredProperty<?> property);

    Component tooltip(ConfiguredProperty<?> property);

    <T> ConfigControlBinding<T> binding(ConfiguredProperty<T> property);

    default <T> T get(ConfiguredProperty<T> property) {
        return binding(property).get();
    }

    default <T> void set(ConfiguredProperty<T> property, T value) {
        binding(property).set(value);
    }

}
