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

        EventMapper<Grow> BEFORE = EventMapper.createUnbound("CropCallback.Grow.Before");
        EventMapper<Grow> AFTER = EventMapper.createUnbound("CropCallback.Grow.After");
    }

}
