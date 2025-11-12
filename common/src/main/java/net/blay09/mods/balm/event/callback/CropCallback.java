package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface CropCallback {

    @FunctionalInterface
    interface Grow {
        void handle(Level level, BlockPos pos, BlockState state);

        EventMapper<CropCallback> PRE = EventMapper.createUnbound("CropCallback.Grow.PRE");
        EventMapper<CropCallback> POST = EventMapper.createUnbound("CropCallback.Grow.POST");
    }

}
