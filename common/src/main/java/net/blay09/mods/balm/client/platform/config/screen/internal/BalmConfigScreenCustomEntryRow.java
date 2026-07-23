package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenEntry;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BalmConfigScreenCustomEntryRow extends BalmConfigScreenRow {
    private final BiFunction<BalmConfigScreen, BalmConfigScreenRowState, BalmConfigScreenEntry> entryFactory;
    private final Predicate<String> filterPredicate;

    public BalmConfigScreenCustomEntryRow(BiFunction<BalmConfigScreen, BalmConfigScreenRowState, BalmConfigScreenEntry> entryFactory, Predicate<String> filterPredicate, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        super(visibilityPredicate);
        this.entryFactory = entryFactory;
        this.filterPredicate = filterPredicate;
    }

    public BiFunction<BalmConfigScreen, BalmConfigScreenRowState, BalmConfigScreenEntry> entryFactory() {
        return entryFactory;
    }

    @Override
    public boolean matchesFilter(String filter) {
        return filterPredicate.test(filter);
    }
}
