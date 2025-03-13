package net.blay09.mods.balm.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class DigSpeedEvent extends BalmEvent {
    private final BlockGetter blockGetter;
    private final BlockPos pos;
    private final BlockState state;
    private final float speed;
    private final Player player;
    private Float speedOverride;

    public DigSpeedEvent(BlockGetter blockGetter, BlockPos pos, BlockState state, float speed, Player player) {
        this.blockGetter = blockGetter;
        this.pos = pos;
        this.player = player;
        this.state = state;
        this.speed = speed;
    }

    public BlockGetter getBlockGetter() {
        return blockGetter;
    }

    public BlockPos getPos() {
        return pos;
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
