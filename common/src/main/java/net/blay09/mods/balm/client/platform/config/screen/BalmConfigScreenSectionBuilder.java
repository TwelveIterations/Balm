package net.blay09.mods.balm.client.platform.config.screen;

import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategory;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface BalmConfigScreenSectionBuilder {
    default BalmConfigScreenSectionBuilder property(ConfiguredProperty<?> property) {
        return property(property, _ -> true);
    }

    BalmConfigScreenSectionBuilder property(ConfiguredProperty<?> property, Predicate<BalmConfigScreenContext> visibilityPredicate);

    default BalmConfigScreenSectionBuilder properties(ConfigCategory category) {
        return properties(category.properties());
    }

    default BalmConfigScreenSectionBuilder properties(ConfiguredProperty<?>... properties) {
        return properties(List.of(properties));
    }

    default BalmConfigScreenSectionBuilder properties(Collection<ConfiguredProperty<?>> properties) {
        properties.forEach(this::property);
        return this;
    }

    default BalmConfigScreenSectionBuilder mergedProperties(Component label, List<ConfiguredProperty<?>> properties, BiFunction<ConfigControlContext, BalmConfigScreenRowState, AbstractWidget> widgetFactory) {
        return mergedProperties(label, Component.empty(), properties, widgetFactory);
    }

    default BalmConfigScreenSectionBuilder mergedProperties(Component label, List<ConfiguredProperty<?>> properties, BiFunction<ConfigControlContext, BalmConfigScreenRowState, AbstractWidget> widgetFactory, Predicate<BalmConfigScreenContext> visibilityPredicate) {
        return mergedProperties(label, Component.empty(), properties, widgetFactory, visibilityPredicate);
    }

    default BalmConfigScreenSectionBuilder mergedProperties(Component label, Component tooltip, List<ConfiguredProperty<?>> properties, BiFunction<ConfigControlContext, BalmConfigScreenRowState, AbstractWidget> widgetFactory) {
        return mergedProperties(label, tooltip, properties, widgetFactory, _ -> true);
    }

    BalmConfigScreenSectionBuilder mergedProperties(Component label, Component tooltip, List<ConfiguredProperty<?>> properties, BiFunction<ConfigControlContext, BalmConfigScreenRowState, AbstractWidget> widgetFactory, Predicate<BalmConfigScreenContext> visibilityPredicate);

    default BalmConfigScreenSectionBuilder button(Component label, Component buttonLabel, Consumer<BalmConfigScreen> onPress) {
        return button(label, Component.empty(), buttonLabel, onPress);
    }

    default BalmConfigScreenSectionBuilder button(Component label, Component tooltip, Component buttonLabel, Consumer<BalmConfigScreen> onPress) {
        return button(label, tooltip, buttonLabel, onPress, filter -> BalmConfigScreenSearch.componentMatches(label, filter)
                || BalmConfigScreenSearch.componentMatches(tooltip, filter));
    }

    default BalmConfigScreenSectionBuilder button(Component label, Component tooltip, Component buttonLabel, Consumer<BalmConfigScreen> onPress, Predicate<String> filterPredicate) {
        return button(label, tooltip, buttonLabel, onPress, filterPredicate, _ -> true);
    }

    BalmConfigScreenSectionBuilder button(Component label, Component tooltip, Component buttonLabel, Consumer<BalmConfigScreen> onPress, Predicate<String> filterPredicate, Predicate<BalmConfigScreenContext> visibilityPredicate);

    default BalmConfigScreenSectionBuilder customEntry(BiFunction<BalmConfigScreen, BalmConfigScreenRowState, ? extends BalmConfigScreenEntry> entryFactory) {
        return customEntry(entryFactory, String::isEmpty);
    }

    default BalmConfigScreenSectionBuilder customEntry(BiFunction<BalmConfigScreen, BalmConfigScreenRowState, ? extends BalmConfigScreenEntry> entryFactory, Predicate<String> filterPredicate) {
        return customEntry(entryFactory, filterPredicate, _ -> true);
    }

    BalmConfigScreenSectionBuilder customEntry(BiFunction<BalmConfigScreen, BalmConfigScreenRowState, ? extends BalmConfigScreenEntry> entryFactory, Predicate<String> filterPredicate, Predicate<BalmConfigScreenContext> visibilityPredicate);
}
