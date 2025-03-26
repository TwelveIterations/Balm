package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;

public class PlayerLogoutEvent extends BalmEvent {
    private final ServerPlayer player;

    public PlayerLogoutEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return player;
    }
}
