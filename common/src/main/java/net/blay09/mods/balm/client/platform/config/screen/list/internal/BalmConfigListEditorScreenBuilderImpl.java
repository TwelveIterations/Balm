package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorContext;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorEntry;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorScreen;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorScreenBuilder;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class BalmConfigListEditorScreenBuilderImpl<T> implements BalmConfigListEditorScreenBuilder<T> {
    private final Screen parent;
    private final BalmConfigScreenContext context;
    private final ConfigControlBinding<? extends Collection<T>> binding;

    private Component title;
    private BiFunction<BalmConfigListEditorContext<T>, BalmConfigListEditorValue<T>, ? extends BalmConfigListEditorEntry<T>> entryFactory = BalmConfigListEditorInlineStringValueEntry::new;
    private BiPredicate<T, String> filterPredicate = (value, filter) -> String.valueOf(value).toLowerCase().contains(filter);

    public BalmConfigListEditorScreenBuilderImpl(Screen parent, BalmConfigScreenContext context, ConfigControlBinding<? extends Collection<T>> binding) {
        this.parent = parent;
        this.context = context;
        this.binding = binding;
        this.title = Component.translatable(ConfigLocalization.forProperty(binding.property()));
    }

    @Override
    public BalmConfigListEditorScreenBuilder<T> title(Component title) {
        this.title = title;
        return this;
    }

    @Override
    public BalmConfigListEditorScreenBuilder<T> customizeLabels(Function<T, Component> labelFactory) {
        entryFactory = (context, value) -> new BalmConfigListEditorInlineStringValueEntry<>(context, value,
                value.value() != null ? labelFactory.apply(value.value()) : Component.empty());
        filterPredicate = (value, filter) -> labelFactory.apply(value).getString().toLowerCase().contains(filter);
        return this;
    }

    @Override
    public BalmConfigListEditorScreenBuilder<T> customizeEntries(BiFunction<BalmConfigListEditorContext<T>, BalmConfigListEditorValue<T>, ? extends BalmConfigListEditorEntry<T>> entryFactory) {
        this.entryFactory = entryFactory;
        return this;
    }

    @Override
    public BalmConfigListEditorScreenBuilder<T> searchable(BiPredicate<T, String> filterPredicate) {
        this.filterPredicate = filterPredicate;
        return this;
    }

    @Override
    public BalmConfigListEditorScreen<T> build() {
        return new BalmConfigListEditorScreen<>(parent, context, binding, title, entryFactory, filterPredicate);
    }
}
