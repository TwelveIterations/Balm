package net.blay09.mods.balm.common.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Optional;
import java.util.function.Function;

public class BalmCodecs {
    public static <T> Codec<T> withAlternative(final Codec<T> primary, final Codec<? extends T> alternative) {
        return Codec.either(
                primary,
                alternative
        ).xmap(
                BalmCodecs::unwrapEither,
                Either::left
        );
    }

    private static <U> U unwrapEither(final Either<? extends U, ? extends U> either) {
        return either.map(Function.identity(), Function.identity());
    }

    public static <E> Codec<E> stringResolver(final Function<E, String> mapper, final Function<String, E> reverseMapper) {
        return Codec.STRING.flatXmap(
                name -> Optional.ofNullable(reverseMapper.apply(name))
                        .map(DataResult::success)
                        .orElseGet(() -> DataResult.error(() -> "Unknown element name:" + name)),
                e -> Optional.ofNullable(mapper.apply(e)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + e))
        );
    }
}
