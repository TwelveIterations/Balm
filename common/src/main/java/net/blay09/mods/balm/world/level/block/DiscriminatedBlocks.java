package net.blay09.mods.balm.world.level.block;

import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

public interface DiscriminatedBlocks<T> extends Map<@Nullable T, DeferredBlock> {
    Stream<Map.Entry<T, DeferredBlock>> filterNonNullDiscriminatorEntries();

    Stream<DeferredBlock> filterNonNullDiscriminators();

    Stream<DeferredBlock> sortedValues(Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default Stream<DeferredBlock> sortedValues() {
        return sortedValues((Comparator<T>) Comparator.nullsFirst(Comparator.naturalOrder()));
    }

    static <T> String prefix(@Nullable T value, String name) {
        return value == null ? name : value + "_" + name;
    }

    static <T> String suffix(String name, @Nullable T value) {
        return value == null ? name : name + "_" + value;
    }
}
