package net.blay09.mods.balm.api.config;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public interface LegacyStringRepresentable extends StringRepresentable {
    /*static <E extends Enum<E>> LegacyEnumCodec<E> fromLegacyEnumWithMapping(Supplier<E[]> enumValuesSupplier, Function<String, String> keyFunction) {
        final var enumValues = enumValuesSupplier.get();
        final var nameLookup = createLegacyNameLookup(enumValues, keyFunction);
        return new LegacyEnumCodec<E>(enumValues, nameLookup);
    }

    static <E extends Enum<E>> LegacyEnumCodec<E> fromLegacyEnum(Supplier<E[]> elementsSupplier) {
        return fromLegacyEnumWithMapping(elementsSupplier, Function.identity());
    }*/

    static <T extends Enum<T>> Codec<T> fromLegacyValues(Supplier<T[]> valuesSupplier) {
        final var values = valuesSupplier.get();
        final var nameLookup = createLegacyNameLookup(values, Function.identity());
        final var indexLookup = Util.createIndexLookup(Arrays.asList(values));
        return new LegacyStringRepresentableCodec<>(values, nameLookup, indexLookup);
    }

    private static <T extends Enum<T>> Function<String, T> createLegacyNameLookup(T[] values, Function<String, String> keyFunction) {
        if (values.length > 16) {
            final var map = Arrays.stream(values).collect(Collectors.toMap((value) -> keyFunction.apply(getSerializedName(value)), Function.identity()));
            return (name) -> name == null ? null : map.get(name);
        } else {
            return (name) -> {
                for (final var value : values) {
                    if (keyFunction.apply(getSerializedName(value)).equals(name)) {
                        return value;
                    }
                }

                return null;
            };
        }
    }

    private static String getSerializedName(Enum<?> enumValue) {
        if (enumValue instanceof StringRepresentable stringRepresentable) {
            return stringRepresentable.getSerializedName();
        } else {
            return enumValue.name().toLowerCase(Locale.ROOT);
        }
    }

    class LegacyStringRepresentableCodec<S extends Enum<S>> implements Codec<S> {
        private final Codec<S> codec;

        public LegacyStringRepresentableCodec(S[] values, Function<String, S> nameLookup, ToIntFunction<S> indexLookup) {
            this.codec = ExtraCodecs.orCompressed(Codec.stringResolver(LegacyStringRepresentable::getSerializedName, nameLookup),
                    ExtraCodecs.idResolverCodec(indexLookup, (p_304986_) -> p_304986_ >= 0 && p_304986_ < values.length ? values[p_304986_] : null, -1));
        }

        public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T value) {
            return this.codec.decode(ops, value);
        }

        public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
            return this.codec.encode(input, ops, prefix);
        }
    }
}
