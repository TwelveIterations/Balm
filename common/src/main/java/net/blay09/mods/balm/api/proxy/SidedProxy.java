package net.blay09.mods.balm.api.proxy;

import java.lang.reflect.InvocationTargetException;

public class SidedProxy<T> {
    private final String commonName;
    private final String clientName;
    private T proxy;

    public SidedProxy(String commonName, String clientName) {
        this.commonName = commonName;
        this.clientName = clientName;
    }

    public void resolveCommon() throws ProxyResolutionException {
        try {
            proxy = (T) Class.forName(commonName).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new ProxyResolutionException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void resolveClient() throws ProxyResolutionException {
        try {
            proxy = (T) Class.forName(clientName).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new ProxyResolutionException(e);
        }
    }

    public T get() {
        if (proxy == null) {
            throw new IllegalStateException("Tried to access proxy before it was resolved");
        }

        return proxy;
    }
}
