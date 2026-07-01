package net.blay09.mods.balm.common.proxy;

import net.blay09.mods.balm.api.proxy.ModProxy;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
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

    private final Function<String, Optional<String>> modVersionProvider;
    private final List<ModEntry<T>> proxies = new ArrayList<>();
    private final @Nullable ResourceLocation identifier;
    @Nullable
    private Function<List<T>, T> multiplexer;
    private @Nullable T fallback;

    public ModProxyImpl(Function<String, Optional<String>> modVersionProvider) {
        this(modVersionProvider, null);
    }

    public ModProxyImpl(Function<String, Optional<String>> modVersionProvider, @Nullable ResourceLocation identifier) {
        this.modVersionProvider = modVersionProvider;
        this.identifier = identifier;
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
            if (effectiveProxies.size() > 1) {
                final var proxy = multiplexer.apply(effectiveProxies);
                logger.info("Mod proxy {} resolved as {}", identifier != null ? identifier : "<unnamed>", proxy);
                return proxy;
            }
            final var proxy = effectiveProxies.get(0);
            logger.info("Mod proxy {} resolved as {}", identifier != null ? identifier : "<unnamed>", proxy);
            return proxy;
        }

        for (final var applicableProxy : applicableProxies) {
            try {
                final var proxy = applicableProxy.proxy.get();
                logger.info("Mod proxy {} resolved as {}", identifier != null ? identifier : "<unnamed>", proxy);
                return proxy;
            } catch (Exception e) {
                logger.error("Failed to instantiate proxy {}", identifier != null ? identifier : "<unnamed>", e);
            }
        }

        if (fallback != null) {
            logger.info("Mod proxy {} resolved as {}", identifier != null ? identifier : "<unnamed>", fallback);
        } else {
            logger.warn("No applicable proxy found for {}", identifier != null ? identifier : "<unnamed>");
        }
        return fallback;
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
