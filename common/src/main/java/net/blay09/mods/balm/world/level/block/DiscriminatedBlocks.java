package net.blay09.mods.balm.world.level.block;

import net.minecraft.util.StringUtil;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public interface DiscriminatedBlocks<T> extends Map<@Nullable T, DeferredBlock> {
    Stream<Map.Entry<@Nullable T, DeferredBlock>> sortedEntries(Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default Stream<Map.Entry<@Nullable T, DeferredBlock>> sortedEntries() {
        return sortedEntries((Comparator<T>) Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    default Stream<DeferredBlock> sortedValues(Comparator<T> comparator) {
        return sortedEntries(comparator).map(Entry::getValue);
    }

    default Stream<DeferredBlock> sortedValues() {
        return sortedEntries().map(Entry::getValue);
    }

    static <T> String prefix(@Nullable T value, String name) {
        return value == null || StringUtil.isBlank(Objects.toString(value)) ? name : value + "_" + name;
    }

    static <T> String suffix(String name, @Nullable T value) {
        return value == null || StringUtil.isBlank(Objects.toString(value)) ? name : name + "_" + value;
    }

    static <T> Function<T, String> prefixer(String name) {
        return prefixer(name, null);
    }

    static <T> Function<T, String> suffixer(String name) {
        return suffixer(name, null);
    }

    static <T> Function<T, String> prefixer(String name, @Nullable T skip) {
        return it -> prefix(it != skip ? it : null, name);
    }

    static <T> Function<T, String> suffixer(String name, @Nullable T skip) {
        return it -> suffix(name, it != skip ? it : null);
    }
}
