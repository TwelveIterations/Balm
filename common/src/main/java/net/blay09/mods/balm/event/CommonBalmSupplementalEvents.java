package net.blay09.mods.balm.event;

import net.blay09.mods.balm.event.callback.ServerLifecycleCallback;

public class CommonBalmSupplementalEvents {
    public static final Event<ServerLifecycleCallback.Reloading> SERVER_RELOADING = EventFactory.createArrayBacked(ServerLifecycleCallback.Reloading.class, (listeners) -> (server, resources) -> {
        for (final var listener : listeners) {
            listener.handle(server, resources);
        }
    });

    public static final Event<ServerLifecycleCallback> SERVER_RELOADED = EventFactory.createArrayBacked(ServerLifecycleCallback.class, (listeners) -> (server) -> {
        for (final var listener : listeners) {
            listener.handle(server);
        }
    });
}
