package net.blay09.mods.balm.platform.internal;

import net.blay09.mods.balm.platform.PlatformProxy;

import java.lang.reflect.InvocationTargetException;

public class PlatformProxyImpl<T> implements PlatformProxy<T> {
    private final String platform;
    private String clazzName;

    public PlatformProxyImpl(String platform) {
        this.platform = platform;
    }

    @Override
    public PlatformProxy<T> with(String platform, String clazzName) {
        if (this.platform.equals(platform)) {
            this.clazzName = clazzName;
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T build() {
        try {
            return (T) Class.forName(clazzName).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to instantiate platform proxy " + clazzName, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate platform proxy, missing no-arg constructor in " + clazzName, e);
        }
    }
}
