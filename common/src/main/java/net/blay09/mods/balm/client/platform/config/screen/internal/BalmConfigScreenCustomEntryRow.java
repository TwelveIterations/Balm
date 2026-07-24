package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenEntry;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BalmConfigScreenCustomEntryRow extends BalmConfigScreenRow {
    private final BiFunction<BalmConfigScreen, BalmConfigScreenRowState, BalmConfigScreenEntry> entryFactory;
    private final List<ConfiguredProperty<?>> properties;
    private final Predicate<String> filterPredicate;

    public BalmConfigScreenCustomEntryRow(List<ConfiguredProperty<?>> properties, BiFunction<BalmConfigScreen, BalmConfigScreenRowState, BalmConfigScreenEntry> entryFactory, Predicate<String> filterPredicate, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        super(visibilityPredicate);
        this.properties = properties;
        this.entryFactory = entryFactory;
        this.filterPredicate = filterPredicate;
    }

    public BiFunction<BalmConfigScreen, BalmConfigScreenRowState, BalmConfigScreenEntry> entryFactory() {
        return entryFactory;
    }

    @Override
    public List<ConfiguredProperty<?>> properties() {
        return properties;
    }

    @Override
    public boolean matchesFilter(String filter) {
        return filterPredicate.test(filter);
    }
}
