package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class BalmConfigScreenButtonRow extends BalmConfigScreenRow {
    private final Component label;
    private final Component tooltip;
    private final Component buttonLabel;
    private final Consumer<BalmConfigScreen> onPress;
    private final Predicate<String> filterPredicate;

    public BalmConfigScreenButtonRow(Component label, Component tooltip, Component buttonLabel, Consumer<BalmConfigScreen> onPress, Predicate<String> filterPredicate, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        super(visibilityPredicate);
        this.label = label;
        this.tooltip = tooltip;
        this.buttonLabel = buttonLabel;
        this.onPress = onPress;
        this.filterPredicate = filterPredicate;
    }

    public Component label() {
        return label;
    }

    public Component tooltip() {
        return tooltip;
    }

    public Component buttonLabel() {
        return buttonLabel;
    }

    public void onPress(BalmConfigScreen screen) {
        onPress.accept(screen);
    }

    @Override
    public boolean matchesFilter(String filter) {
        return filterPredicate.test(filter);
    }
}
