package net.blay09.mods.balm.fabric.event;

import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class FabricBalmSupplementalEvents {
    public static final Event<ServerPlayerTickCallback> SERVER_PLAYER_TICK_PRE = EventFactory.createArrayBacked(ServerPlayerTickCallback.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ServerPlayerTickCallback> SERVER_PLAYER_TICK_POST = EventFactory.createArrayBacked(ServerPlayerTickCallback.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ServerEntityTickCallback> SERVER_ENTITY_TICK_PRE = EventFactory.createArrayBacked(ServerEntityTickCallback.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static final Event<ServerEntityTickCallback> SERVER_ENTITY_TICK_POST = EventFactory.createArrayBacked(ServerEntityTickCallback.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static void initialize() {
        ServerTickEvents.START_WORLD_TICK.register(level -> {
            if (SERVER_PLAYER_TICK_PRE.hasHandlers()) {
                for (final var player : level.players()) {
                    SERVER_PLAYER_TICK_PRE.invoker().handle(player);
                }
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            if (SERVER_PLAYER_TICK_POST.hasHandlers()) {
                for (final var player : level.players()) {
                    SERVER_PLAYER_TICK_POST.invoker().handle(player);
                }
            }
        });

        ServerTickEvents.START_WORLD_TICK.register(level -> {
            if (SERVER_ENTITY_TICK_PRE.hasHandlers()) {
                for (final var entity : level.getAllEntities()) {
                    SERVER_ENTITY_TICK_PRE.invoker().handle(entity);
                }
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            if (SERVER_ENTITY_TICK_POST.hasHandlers()) {
                for (final var entity : level.getAllEntities()) {
                    SERVER_ENTITY_TICK_POST.invoker().handle(entity);
                }
            }
        });
    }
}
