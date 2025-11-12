package net.blay09.mods.balm.fabric.client.event;

import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.mixin.ClientLevelAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricBalmSupplementalClientEvents {
    public static final Event<ClientTickCallback.Player> CLIENT_PLAYER_TICK_PRE = EventFactory.createArrayBacked(ClientTickCallback.Player.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ClientTickCallback.Player> CLIENT_PLAYER_TICK_POST = EventFactory.createArrayBacked(ClientTickCallback.Player.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ClientTickCallback.Entity> CLIENT_ENTITY_TICK_PRE = EventFactory.createArrayBacked(ClientTickCallback.Entity.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static final Event<ClientTickCallback.Entity> CLIENT_ENTITY_TICK_POST = EventFactory.createArrayBacked(ClientTickCallback.Entity.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static void initialize() {
        ClientTickEvents.START_WORLD_TICK.register(level -> {
            if (CLIENT_PLAYER_TICK_PRE.hasHandlers()) {
                for (final var player : level.players()) {
                    CLIENT_PLAYER_TICK_PRE.invoker().handle(player);
                }
            }
        });
        ClientTickEvents.END_WORLD_TICK.register(level -> {
            if (CLIENT_PLAYER_TICK_POST.hasHandlers()) {
                for (final var player : level.players()) {
                    CLIENT_PLAYER_TICK_POST.invoker().handle(player);
                }
            }
        });

        ClientTickEvents.START_WORLD_TICK.register(level -> {
            if (CLIENT_ENTITY_TICK_PRE.hasHandlers()) {
                ((ClientLevelAccessor) level).getTickingEntities().forEach(entity -> CLIENT_ENTITY_TICK_PRE.invoker().handle(entity));
            }
        });
        ClientTickEvents.END_WORLD_TICK.register(level -> {
            if (CLIENT_ENTITY_TICK_POST.hasHandlers()) {
                ((ClientLevelAccessor) level).getTickingEntities().forEach(entity -> CLIENT_ENTITY_TICK_POST.invoker().handle(entity));
            }
        });
    }
}
