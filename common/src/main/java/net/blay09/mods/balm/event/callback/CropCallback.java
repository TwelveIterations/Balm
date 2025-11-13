package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.event.EventHandling;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface CropCallback {

    @FunctionalInterface
    interface Grow {
        EventHandling handle(LevelAccessor level, BlockPos pos, BlockState state);

        EventMapper<Grow> PRE = EventMapper.createUnbound("CropCallback.Grow.PRE");
        EventMapper<Grow> POST = EventMapper.createUnbound("CropCallback.Grow.POST");
    }

}
