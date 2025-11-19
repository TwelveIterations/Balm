package net.blay09.mods.balm.api.event;

import net.blay09.mods.balm.event.callback.ServerTickCallback;
import net.minecraft.server.level.ServerPlayer;

/**
 * @deprecated Use {@link ServerTickCallback.ServerPlayerTick} instead.
 */
@Deprecated
public interface ServerPlayerTickHandler {
    void handle(ServerPlayer player);
}
