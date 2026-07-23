package net.blay09.mods.balm.client.platform.config.screen.list;

import net.blay09.mods.balm.client.platform.config.screen.list.internal.BalmConfigListEditorValue;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public interface BalmConfigListEditorScreenBuilder<T> {
    BalmConfigListEditorScreenBuilder<T> title(Component title);

    BalmConfigListEditorScreenBuilder<T> customizeLabels(Function<T, Component> labelFactory);

    BalmConfigListEditorScreenBuilder<T> customizeEntries(BiFunction<BalmConfigListEditorContext<T>, BalmConfigListEditorValue<T>, ? extends BalmConfigListEditorEntry<T>> entryFactory);

    BalmConfigListEditorScreenBuilder<T> searchable(BiPredicate<T, String> filterPredicate);

    BalmConfigListEditorScreen<T> build();
}
