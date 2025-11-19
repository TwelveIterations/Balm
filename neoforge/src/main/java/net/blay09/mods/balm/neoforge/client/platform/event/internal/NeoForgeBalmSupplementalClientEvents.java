package net.blay09.mods.balm.neoforge.client.platform.event.internal;

import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.client.platform.event.callback.ScreenCallback;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;

public class NeoForgeBalmSupplementalClientEvents {
    public static final Event<ClientLifecycleCallback.Started> CLIENT_STARTED = EventFactory.createArrayBacked(ClientLifecycleCallback.Started.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ScreenCallback.Init> SCREEN_INIT_PRE = EventFactory.createArrayBacked(ScreenCallback.Init.class, (listeners) -> (screen) -> {
        for (final var listener : listeners) {
            listener.handle(screen);
        }
    });

    public static final Event<ScreenCallback.Init> SCREEN_INIT_POST = EventFactory.createArrayBacked(ScreenCallback.Init.class, (listeners) -> (screen) -> {
        for (final var listener : listeners) {
            listener.handle(screen);
        }
    });
}
