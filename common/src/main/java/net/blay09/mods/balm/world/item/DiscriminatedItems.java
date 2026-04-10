package net.blay09.mods.balm.world.item;

import net.minecraft.util.StringUtil;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
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

    @Deprecated
    static <T> String prefix(@Nullable T value, String name) {
        return value == null || StringUtil.isBlank(Objects.toString(value)) ? name : value + "_" + name;
    }

    @Deprecated
    static <T> String suffix(String name, @Nullable T value) {
        return value == null || StringUtil.isBlank(Objects.toString(value)) ? name : name + "_" + value;
    }

    @Deprecated
    static <T> Function<T, String> prefixer(String name) {
        return prefixer(name, null);
    }

    @Deprecated
    static <T> Function<T, String> suffixer(String name) {
        return suffixer(name, null);
    }

    @Deprecated
    static <T> Function<T, String> prefixer(String name, @Nullable T skip) {
        return it -> prefix(it != skip ? it : null, name);
    }

    @Deprecated
    static <T> Function<T, String> suffixer(String name, @Nullable T skip) {
        return it -> suffix(name, it != skip ? it : null);
    }

    static <T> Function<T, String> prefixWith(String name) {
        return it -> name + "_" + it;
    }

    static <T> Function<T, String> suffixWith(String name) {
        return it -> it + "_" + name;
    }

    static <T> Function<T, String> surroundWith(String prefix, String suffix) {
        return it -> prefix + "_" + it + "_" + suffix;
    }
}
