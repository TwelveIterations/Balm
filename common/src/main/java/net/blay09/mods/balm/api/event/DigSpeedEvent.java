package net.blay09.mods.balm.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class DigSpeedEvent extends BalmEvent {
    private final Player player;
    private final BlockState state;
    private final float speed;
    private Float speedOverride;

    public DigSpeedEvent(Player player, BlockState state, float speed) {
        this.player = player;
        this.state = state;
        this.speed = speed;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockState getState() {
        return state;
    }

    public float getSpeed() {
        return speed;
    }

    public Float getSpeedOverride() {
        return speedOverride;
    }

    public void setSpeedOverride(Float speedOverride) {
        this.speedOverride = speedOverride;
    }
}
