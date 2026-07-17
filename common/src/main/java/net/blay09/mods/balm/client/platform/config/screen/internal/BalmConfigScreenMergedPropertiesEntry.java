package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenLabeledEntry;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BalmConfigScreenMergedPropertiesEntry extends BalmConfigScreenLabeledEntry {
    private final List<ConfiguredProperty<?>> properties;

    public BalmConfigScreenMergedPropertiesEntry(BalmConfigScreenContext context, BalmConfigScreenMergedPropertiesRow row, AbstractWidget control) {
        super(context, row.label(), row.tooltip(), control);
        this.properties = row.properties();
    }

    @Override
    protected @Nullable Component getValidationError() {
        for (final var property : properties) {
            final var error = context.getValidationError(property);
            if (error != null) {
                return error;
            }
        }
        return null;
    }
}
