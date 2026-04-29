package net.blay09.mods.balm.world.item;

import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public interface DiscriminatedItems<T> extends Map<T, DeferredItem> {
    Stream<Map.Entry<T, DeferredItem>> sortedEntries(Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default Stream<Map.Entry<T, DeferredItem>> sortedEntries() {
        return sortedEntries((Comparator<T>) Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    default Stream<DeferredItem> sortedValues(Comparator<T> comparator) {
        return sortedEntries(comparator).map(Entry::getValue);
    }

    default Stream<DeferredItem> sortedValues() {
        return sortedEntries().map(Entry::getValue);
    }

    static <T> Function<@Nullable T, String> prefixWith(String name) {
        return it -> it != null ? name + "_" + it : name;
    }

    static <T> Function<@Nullable T, String> suffixWith(String name) {
        return it -> it != null ? it + "_" + name : name;
    }

    static <T> Function<T, String> surroundWith(String prefix, String suffix) {
        return it -> prefix + "_" + it + "_" + suffix;
    }
}
