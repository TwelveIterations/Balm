package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenWidgetFactory;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenSearch;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Predicate;

public class BalmConfigScreenMergedPropertiesRow extends BalmConfigScreenRow {
    private final Component label;
    private final Component tooltip;
    private final List<ConfiguredProperty<?>> properties;
    private final BalmConfigScreenWidgetFactory widgetFactory;

    public BalmConfigScreenMergedPropertiesRow(Component label, Component tooltip, List<ConfiguredProperty<?>> properties, BalmConfigScreenWidgetFactory widgetFactory, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        super(visibilityPredicate);
        this.label = label;
        this.tooltip = tooltip;
        this.properties = properties;
        this.widgetFactory = widgetFactory;
    }

    public Component label() {
        return label;
    }

    public Component tooltip() {
        return tooltip;
    }

    public BalmConfigScreenWidgetFactory widgetFactory() {
        return widgetFactory;
    }

    @Override
    public List<ConfiguredProperty<?>> properties() {
        return properties;
    }

    @Override
    public boolean matchesFilter(String filter) {
        return BalmConfigScreenSearch.componentMatches(label, filter)
                || BalmConfigScreenSearch.componentMatches(tooltip, filter)
                || properties.stream().anyMatch(property -> BalmConfigScreenSearch.propertyMatches(property, filter));
    }
}
