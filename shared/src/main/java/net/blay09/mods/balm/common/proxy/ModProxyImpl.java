package net.blay09.mods.balm.common.proxy;

import net.blay09.mods.balm.api.proxy.ModProxy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModProxyImpl<T> implements ModProxy<T> {
    public ModProxyImpl(Predicate<String> modLoadedPredicate) {
        this.modLoadedPredicate = modLoadedPredicate;
    }

    private final class ModEntry {
        private final String modId;
        private final String clazzName;
        private final Supplier<T> proxy;

        private ModEntry(String modId, String clazzName, Supplier<T> proxy) {
            this.modId = modId;
            this.clazzName = clazzName;
            this.proxy = proxy;
        }

        public String modId() {
            return modId;
        }

        public String clazzName() {
            return clazzName;
        }

        public Supplier<T> proxy() {
            return proxy;
        }
    }

    private final Predicate<String> modLoadedPredicate;
    private final List<ModEntry> proxies = new ArrayList<>();
    private Function<List<T>, T> multiplexer;
    private T fallback;

    @Override
    @SuppressWarnings("unchecked")
    public ModProxy<T> with(String modId, String clazzName) {
        if (modLoadedPredicate.test(modId)) {
            proxies.add(new ModEntry(modId, clazzName, () -> {
                try {
                    return (T) Class.forName(clazzName).getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                    throw new RuntimeException("Failed to instantiate mod proxy " + clazzName, e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Failed to instantiate mod proxy, missing no-arg constructor in " + clazzName, e);
                }
            }));
        }
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
        if (multiplexer != null && proxies.size() > 1) {
            return multiplexer.apply(proxies.stream().map(ModEntry::proxy).map(Supplier::get).collect(Collectors.toList()));
        }

        if (proxies.isEmpty()) {
            return fallback;
        }

        return proxies.get(0).proxy().get();
    }
}
