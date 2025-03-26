package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PlayerOpenMenuEvent extends BalmEvent {
    private final ServerPlayer player;
    private final AbstractContainerMenu menu;

    public PlayerOpenMenuEvent(ServerPlayer player, AbstractContainerMenu menu) {
        this.player = player;
        this.menu = menu;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public AbstractContainerMenu getMenu() {
        return menu;
    }
}
