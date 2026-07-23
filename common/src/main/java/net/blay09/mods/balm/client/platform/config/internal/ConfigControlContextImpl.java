package net.blay09.mods.balm.client.platform.config.internal;

import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ConfigControlContextImpl implements ConfigControlContext {
    private final int entryWidth;
    private final int entryHeight;
    private final Function<ConfiguredProperty<?>, ConfigControlBinding<?>> bindingFactory;

    public ConfigControlContextImpl(int entryWidth, int entryHeight, Function<ConfiguredProperty<?>, ConfigControlBinding<?>> bindingFactory) {
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
        this.bindingFactory = bindingFactory;
    }

    @Override
    public int entryWidth() {
        return entryWidth;
    }

    @Override
    public int entryHeight() {
        return entryHeight;
    }

    @Override
    public Component label(ConfiguredProperty<?> property) {
        return Component.translatable(ConfigLocalization.forProperty(property));
    }

    @Override
    public Component tooltip(ConfiguredProperty<?> property) {
        return Component.translatable(ConfigLocalization.forPropertyTooltip(property));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ConfigControlBinding<T> binding(ConfiguredProperty<T> property) {
        return (ConfigControlBinding<T>) bindingFactory.apply(property);
    }
}
