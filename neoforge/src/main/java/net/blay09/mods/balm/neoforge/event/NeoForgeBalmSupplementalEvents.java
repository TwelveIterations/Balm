package net.blay09.mods.balm.neoforge.event;

import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.event.callback.ConfigCallback;
import net.blay09.mods.balm.event.callback.ServerPlayerCallback;

public class NeoForgeBalmSupplementalEvents {
    public static final Event<ServerPlayerCallback> SERVER_PLAYER_CONNECTED = EventFactory.createArrayBacked(ServerPlayerCallback.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ConfigCallback> CONFIG_LOADED = EventFactory.createArrayBacked(ConfigCallback.class, (listeners) -> (schema) -> {
        for (final var listener : listeners) {
            listener.handle(schema);
        }
    });

    public static final Event<ConfigCallback> CONFIG_RELOADED = EventFactory.createArrayBacked(ConfigCallback.class, (listeners) -> (schema) -> {
        for (final var listener : listeners) {
            listener.handle(schema);
        }
    });
}
