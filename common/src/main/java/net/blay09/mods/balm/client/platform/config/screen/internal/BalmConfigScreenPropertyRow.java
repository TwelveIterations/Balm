package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;

import java.util.List;
import java.util.function.Predicate;

public class BalmConfigScreenPropertyRow extends BalmConfigScreenRow {
    private final ConfiguredProperty<?> property;

    public BalmConfigScreenPropertyRow(ConfiguredProperty<?> property, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        super(visibilityPredicate);
        this.property = property;
    }

    public ConfiguredProperty<?> property() {
        return property;
    }

    @Override
    public List<ConfiguredProperty<?>> properties() {
        return List.of(property);
    }

    @Override
    public boolean matchesFilter(String filter) {
        return BalmConfigScreenSearch.propertyMatches(property, filter);
    }
}
