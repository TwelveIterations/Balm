package net.blay09.mods.balm.api.provider;

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
