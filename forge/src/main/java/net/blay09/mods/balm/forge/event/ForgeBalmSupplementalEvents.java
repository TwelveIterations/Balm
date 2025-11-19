package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.event.callback.ConfigCallback;
import net.blay09.mods.balm.event.callback.ServerPlayerCallback;

public class ForgeBalmSupplementalEvents {
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
