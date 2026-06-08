package net.blay09.mods.balm.world.level.block;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public interface DiscriminatedBlocks<T> extends Map<T, DeferredBlock> {
    Stream<Entry<T, DeferredBlock>> filterNonNullDiscriminatorEntries();

    Stream<DeferredBlock> filterNonNullDiscriminators();

    /**
     * @deprecated Use {@link #suffixWith(String)} instead.
     */
    @Deprecated
    static <T> String prefix(T value, String name) {
        return value == null ? name : value + "_" + name;
    }

    /**
     * @deprecated Use {@link #prefixWith(String)} instead.
     */
    @Deprecated
    static <T> String suffix(String name, T value) {
        return value == null ? name : name + "_" + value;
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
