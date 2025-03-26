package net.blay09.mods.balm.api.proxy;

public interface PlatformProxy<T> {

    PlatformProxy<T> with(String platform, String clazzName);

    T build();

    default PlatformProxy<T> withFabric(String clazzName) {
        return with(LoaderPlatforms.FABRIC, clazzName);
    }

    default PlatformProxy<T> withForge(String clazzName) {
        return with(LoaderPlatforms.FORGE, clazzName);
    }
}
