package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerPlayerCallback.Respawn} instead.
 */
@Deprecated
public class PlayerRespawnEvent extends BalmEvent {
    private final ServerPlayer oldPlayer;
    private final ServerPlayer newPlayer;

    public PlayerRespawnEvent(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        this.oldPlayer = oldPlayer;
        this.newPlayer = newPlayer;
    }

    public ServerPlayer getOldPlayer() {
        return oldPlayer;
    }

    public ServerPlayer getNewPlayer() {
        return newPlayer;
    }
}
