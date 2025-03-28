package net.blay09.mods.balm.api.proxy;

import net.blay09.mods.balm.api.BalmEnvironment;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class SidedProxy<T> {
    private final Supplier<BalmEnvironment> environmentResolver;
    private final String commonName;
    private final String clientName;
    private T proxy;

    public SidedProxy(Supplier<BalmEnvironment> environmentResolver, String commonName, String clientName) {
        this.environmentResolver = environmentResolver;
        this.commonName = commonName;
        this.clientName = clientName;
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

    @SuppressWarnings("unchecked")
    public T build() {
        final var classNameForEnvironment = switch (environmentResolver.get()) {
            case CLIENT -> clientName;
            case SERVER -> commonName;
        };
        try {
            proxy = (T) Class.forName(classNameForEnvironment).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return proxy;
    }

    /**
     * @deprecated Use {@link #build()} or {@link #buildLazily()} instead.
     */
    @Deprecated
    public T get() {
        if (proxy == null) {
            proxy = build();
        }
        return proxy;
    }
}
