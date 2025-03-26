package net.blay09.mods.balm.api.proxy;

public class ProxyResolutionException extends Exception {
    public ProxyResolutionException() {
    }

    public ProxyResolutionException(String message) {
        super(message);
    }

    public ProxyResolutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyResolutionException(Throwable cause) {
        super(cause);
    }

    public ProxyResolutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
