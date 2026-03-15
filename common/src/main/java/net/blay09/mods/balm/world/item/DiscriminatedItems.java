package net.blay09.mods.balm.world.item;

import net.minecraft.util.StringUtil;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public interface DiscriminatedItems<T> extends Map<@Nullable T, DeferredItem> {
    Stream<Map.Entry<T, DeferredItem>> filterNonNullDiscriminatorEntries();

    Stream<DeferredItem> filterNonNullDiscriminators();

    Stream<DeferredItem> sortedValues(Comparator<T> comparator);

    @SuppressWarnings("unchecked")
    default Stream<DeferredItem> sortedValues() {
        return sortedValues((Comparator<T>) Comparator.nullsFirst(Comparator.naturalOrder()));
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
