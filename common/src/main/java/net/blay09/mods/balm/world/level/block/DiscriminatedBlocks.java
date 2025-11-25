package net.blay09.mods.balm.world.level.block;

import java.util.Map;
import java.util.stream.Stream;

public interface DiscriminatedBlocks<T> extends Map<T, DeferredBlock> {
    Stream<Entry<T, DeferredBlock>> filterNonNullDiscriminatorEntries();

    Stream<DeferredBlock> filterNonNullDiscriminators();

    static <T> String prefix(T value, String name) {
        return value == null ? name : value + "_" + name;
    }

    static <T> String suffix(String name, T value) {
        return value == null ? name : name + "_" + value;
    }
}
