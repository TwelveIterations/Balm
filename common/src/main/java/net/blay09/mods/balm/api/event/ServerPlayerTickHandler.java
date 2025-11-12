package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerTickCallback.Player} instead.
 */
@Deprecated
public interface ServerPlayerTickHandler {
    void handle(ServerPlayer player);
}
