package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenSectionBuilder;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenEntry;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BalmConfigScreenSectionBuilderImpl implements BalmConfigScreenSectionBuilder {
    private final List<BalmConfigScreenRow> rows = new ArrayList<>();

    @Override
    public BalmConfigScreenSectionBuilder property(ConfiguredProperty<?> property, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        rows.add(new BalmConfigScreenPropertyRow(property, visibilityPredicate));
        return this;
    }

    @Override
    public BalmConfigScreenSectionBuilder mergedProperties(Component label, Component tooltip, List<ConfiguredProperty<?>> properties, BiFunction<ConfigControlContext, BalmConfigScreenRowState, AbstractWidget> widgetFactory, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        if (properties.isEmpty()) {
            throw new IllegalArgumentException("Merged property rows must declare at least one property");
        }
        rows.add(new BalmConfigScreenMergedPropertiesRow(label, tooltip, List.copyOf(properties), widgetFactory, visibilityPredicate));
        return this;
    }

    @Override
    public BalmConfigScreenSectionBuilder customEntry(BiFunction<BalmConfigScreen, BalmConfigScreenRowState, ? extends BalmConfigScreenEntry> entryFactory, Predicate<String> filterPredicate, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        rows.add(new BalmConfigScreenCustomEntryRow(entryFactory::apply, filterPredicate, visibilityPredicate));
        return this;
    }

    public List<BalmConfigScreenRow> rows() {
        return rows;
    }
}
