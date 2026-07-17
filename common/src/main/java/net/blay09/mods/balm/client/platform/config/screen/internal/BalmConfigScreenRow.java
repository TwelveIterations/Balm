package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;

import java.util.List;
import java.util.function.Predicate;

public abstract class BalmConfigScreenRow {
    private final BalmConfigScreenRowState state = new BalmConfigScreenRowState();
    private final Predicate<BalmConfigScreenContext> visibilityPredicate;

    protected BalmConfigScreenRow(Predicate<BalmConfigScreenContext> visibilityPredicate) {
        this.visibilityPredicate = visibilityPredicate;
    }

    public BalmConfigScreenRowState state() {
        return state;
    }

    public boolean isVisible(BalmConfigScreenContext context) {
        return visibilityPredicate.test(context);
    }

    public List<ConfiguredProperty<?>> properties() {
        return List.of();
    }

    public abstract boolean matchesFilter(String filter);
}
