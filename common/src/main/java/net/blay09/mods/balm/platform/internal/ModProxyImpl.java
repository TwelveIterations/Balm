package net.blay09.mods.balm.platform.internal;

import net.blay09.mods.balm.platform.ModProxy;
import net.blay09.mods.balm.platform.ModInfo;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModProxyImpl<T> implements ModProxy<T> {

    private final Logger logger = LoggerFactory.getLogger(ModProxyImpl.class);

    private final Function<String, Optional<ModInfo>> modInfoProvider;
    private final List<ModEntry<T>> proxies = new ArrayList<>();
    @Nullable
    private Function<List<T>, T> multiplexer;
    private @Nullable T fallback;

    public ModProxyImpl(Function<String, Optional<ModInfo>> modInfoProvider) {
        this.modInfoProvider = modInfoProvider;
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
            final var effectiveProxies = new ArrayList<T>();
            for (final var applicableProxy : applicableProxies) {
                try {
                    effectiveProxies.add(applicableProxy.proxy.get());
                } catch (Exception e) {
                    logger.error("Failed to instantiate proxy", e);
                }
            }
            if(effectiveProxies.size() > 1) {
                return multiplexer.apply(effectiveProxies);
            }
            return effectiveProxies.getFirst();
        }

        for (final var applicableProxy : applicableProxies) {
            try {
                return applicableProxy.proxy.get();
            } catch (Exception e) {
                logger.error("Failed to instantiate proxy", e);
            }
        }

        return fallback;
    }

    private boolean isApplicable(ModEntry<T> proxy) {
        final var modInfo = modInfoProvider.apply(proxy.modId);
        return modInfo.isPresent() && (proxy.versionRange == null || proxy.versionRange.contains(modInfo.get().versionString()));
    }

    public Supplier<T> buildLazily() {
        return new Supplier<>() {
            @Nullable
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
