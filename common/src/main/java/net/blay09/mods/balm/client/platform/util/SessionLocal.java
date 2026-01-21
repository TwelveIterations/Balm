package net.blay09.mods.balm.client.platform.util;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import org.jspecify.annotations.Nullable;

/**
 * A container for a value which is reset when connecting to or disconnecting from a server.
 *
 * @param <T> The type stored in this container.
 */
public class SessionLocal<T extends @Nullable Object> {
    private static final Set<SessionLocal<?>> INSTANCES = Collections.newSetFromMap(new WeakHashMap<>());

    /**
     * The current value held by this container.
     * This is reset every time the client connects to or disconnects from a server.
     */
    public T value;

    private final Supplier<T> defaultSupplier;

    static {
        ClientLifecycleCallback.ConnectedToServer.EVENT.register(client -> INSTANCES.forEach(SessionLocal::reset));
        ClientLifecycleCallback.DisconnectedFromServer.EVENT.register(client -> INSTANCES.forEach(SessionLocal::reset));
    }

    /**
     * Creates a new {@link SessionLocal} with the provided default supplier.
     *
     * @param defaultSupplier Supplies the default value initially
     *                        and every time the client connects to or disconnects from a server.
     */
    public SessionLocal(Supplier<T> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
        value = defaultSupplier.get();
        INSTANCES.add(this);
    }

    private void reset() {
        value = defaultSupplier.get();
    }
}
