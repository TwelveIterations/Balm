package net.blay09.mods.balm.event;

import net.blay09.mods.balm.event.callback.BlockCallback;
import net.blay09.mods.balm.event.callback.ServerLifecycleCallback;

public class BalmSupplementalEvents {
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

    public static final Event<BlockCallback.DigSpeed> BLOCK_DIG_SPEED = EventFactory.createArrayBacked(BlockCallback.DigSpeed.class, (listeners) -> (blockGetter, pos, state, player, speed) -> {
        float newSpeed = speed;
        for (final var listener : listeners) {
            newSpeed = listener.handle(blockGetter, pos, state, player, newSpeed);
        }
        return newSpeed;
    });
}
