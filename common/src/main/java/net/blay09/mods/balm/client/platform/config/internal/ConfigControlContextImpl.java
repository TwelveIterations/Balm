package net.blay09.mods.balm.client.platform.config.internal;

import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.network.chat.Component;

public class ConfigControlContextImpl implements ConfigControlContext {
    private final int entryWidth;
    private final int entryHeight;

    public ConfigControlContextImpl(int entryWidth, int entryHeight) {
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
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
}
