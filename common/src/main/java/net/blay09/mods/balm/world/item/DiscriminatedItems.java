package net.blay09.mods.balm.world.item;

import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

public interface DiscriminatedItems<T> extends Map<@Nullable T, DeferredItem> {
    Stream<Map.Entry<T, DeferredItem>> filterNonNullDiscriminatorEntries();

    Stream<DeferredItem> filterNonNullDiscriminators();

    Stream<DeferredItem> sortedValues(Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default Stream<DeferredItem> sortedValues() {
        return sortedValues((Comparator<T>) Comparator.nullsLast(Comparator.naturalOrder()));
    }

    static <T> String prefix(@Nullable T value, String name) {
        return value == null ? name : value + "_" + name;
    }

    static <T> String suffix(String name, @Nullable T value) {
        return value == null ? name : name + "_" + value;
    }
}
