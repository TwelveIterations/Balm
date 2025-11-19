package net.blay09.mods.balm.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.CropCallback.Grow} instead.
 */
@Deprecated
public class CropGrowEvent extends BalmEvent {
    private final Level level;
    private final BlockPos pos;
    private final BlockState state;

    public CropGrowEvent(Level level, BlockPos pos, BlockState state) {
        this.level = level;
        this.pos = pos;
        this.state = state;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.CropCallback.Grow#BEFORE} instead.
     */
    @Deprecated
    public static class Pre extends CropGrowEvent {
        public Pre(Level level, BlockPos pos, BlockState state) {
            super(level, pos, state);
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.CropCallback.Grow#AFTER} instead.
     */
    @Deprecated
    public static class Post extends CropGrowEvent {
        public Post(Level level, BlockPos pos, BlockState state) {
            super(level, pos, state);
        }
    }
}
