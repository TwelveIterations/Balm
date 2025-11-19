package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerPlayerCallback#LOGIN} instead.
 */
@Deprecated
public class PlayerLoginEvent extends BalmEvent {
    private final ServerPlayer player;

    public PlayerLoginEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }
}
