package net.blay09.mods.balm.api.provider;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
 */
@Deprecated(forRemoval = true, since = "1.21.5")
public class BalmProvider<T> {
    private final Class<T> providerClass;
    private final T instance;

    public BalmProvider(Class<T> providerClass, T instance) {
        this.providerClass = providerClass;
        this.instance = instance;
    }

    public Class<T> getProviderClass() {
        return providerClass;
    }

    public T getInstance() {
        return instance;
    }
}
