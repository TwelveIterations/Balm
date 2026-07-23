package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenLabeledEntry;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class BalmConfigScreenPropertyEntry extends BalmConfigScreenLabeledEntry {
    private final ConfiguredProperty<?> property;

    public BalmConfigScreenPropertyEntry(BalmConfigScreenContext context, ConfiguredProperty<?> property, AbstractWidget control) {
        super(context, Component.translatable(ConfigLocalization.forProperty(property)), Component.translatable(ConfigLocalization.forPropertyTooltip(property)), control);
        this.property = property;
    }

    @Override
    protected @Nullable Component getValidationError() {
        return context.getValidationError(property);
    }

}
