package net.blay09.mods.balm.common.proxy;

import net.blay09.mods.balm.api.proxy.ModProxy;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModProxyImpl<T> implements ModProxy<T> {

    private final Function<String, Optional<String>> modVersionProvider;
    private final List<ModEntry<T>> proxies = new ArrayList<>();
    @Nullable
    private Function<List<T>, T> multiplexer;
    private @Nullable T fallback;

    public ModProxyImpl(Function<String, Optional<String>> modVersionProvider) {
        this.modVersionProvider = modVersionProvider;
    }

    @Override
    public ModProxy<T> with(String modId, String clazzName) {
        return with(modId, null, clazzName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ModProxy<T> with(String modId, @Nullable String versionRange, String clazzName) {
        proxies.add(new ModEntry<>(modId, versionRange != null ? VersionRange.parse(versionRange.trim()) : null, clazzName, () -> {
            try {
                return (T) Class.forName(clazzName).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                throw new RuntimeException("Failed to instantiate mod proxy " + clazzName, e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to instantiate mod proxy, missing no-arg constructor in " + clazzName, e);
            }
        }));

        return this;
    }

    @Override
    public ModProxy<T> withMultiplexer(Function<List<T>, T> multiplexer) {
        this.multiplexer = multiplexer;
        return this;
    }

    @Override
    public ModProxy<T> withFallback(T fallback) {
        this.fallback = fallback;
        return this;
    }

    @Override
    public T build() {
        final var applicableProxies = proxies.stream().filter(this::isApplicable).toList();
        if (multiplexer != null && applicableProxies.size() > 1) {
            return multiplexer.apply(applicableProxies.stream().map(ModEntry::proxy).map(Supplier::get).collect(Collectors.toList()));
        }

        if (applicableProxies.isEmpty()) {
            return fallback;
        }

        return applicableProxies.get(0).proxy().get();
    }

    private boolean isApplicable(ModEntry<T> proxy) {
        final var modVersion = modVersionProvider.apply(proxy.modId);
        return modVersion.isPresent() && (proxy.versionRange == null || proxy.versionRange.contains(modVersion.get()));
    }

    public Supplier<T> buildLazily() {
        return new Supplier<>() {
            private T instance;

            @Override
            public T get() {
                if (instance == null) {
                    instance = build();
                }
                return instance;
            }
        };
    }

    public record ModEntry<T>(String modId, @Nullable VersionRange versionRange, String clazzName, Supplier<T> proxy) {
    }
}
