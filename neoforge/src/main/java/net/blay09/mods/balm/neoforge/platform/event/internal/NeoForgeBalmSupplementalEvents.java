package net.blay09.mods.balm.neoforge.platform.event.internal;

import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.callback.ConfigCallback;
import net.blay09.mods.balm.platform.event.callback.ServerPlayerCallback;

public class NeoForgeBalmSupplementalEvents {
    public static final Event<ServerPlayerCallback.Connected> SERVER_PLAYER_CONNECTED = EventFactory.createArrayBacked(ServerPlayerCallback.Connected.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ConfigCallback.Loaded> CONFIG_LOADED = EventFactory.createArrayBacked(ConfigCallback.Loaded.class, (listeners) -> (schema) -> {
        for (final var listener : listeners) {
            listener.handle(schema);
        }
    });

    public static final Event<ConfigCallback.Reloaded> CONFIG_RELOADED = EventFactory.createArrayBacked(ConfigCallback.Reloaded.class, (listeners) -> (schema) -> {
        for (final var listener : listeners) {
            listener.handle(schema);
        }
    });
}
