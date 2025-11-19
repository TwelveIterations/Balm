package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerPlayerCallback#CONNECTED} instead.
 */
@Deprecated
public class PlayerConnectedEvent extends BalmEvent {
    private final ServerPlayer player;

    public PlayerConnectedEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }
}
