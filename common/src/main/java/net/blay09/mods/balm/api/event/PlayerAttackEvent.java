package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class PlayerAttackEvent extends BalmEvent {
    private final Player player;
    private final Entity target;

    public PlayerAttackEvent(Player player, Entity target) {
        this.player = player;
        this.target = target;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getTarget() {
        return target;
    }
}
