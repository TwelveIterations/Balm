package net.blay09.mods.balm.platform;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModProxy<T> {

    ModProxy<T> with(String platform, String clazzName);

    ModProxy<T> withMultiplexer(Function<List<T>, T> multiplexer);

    ModProxy<T> withFallback(T fallback);

    T build();

    Supplier<T> buildLazily();
}
