package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface CropCallback {
    interface Grow {
        @FunctionalInterface
        interface Before {
            enum Result {
                DEFAULT,
                DO_NOT_GROW,
                GROW
            }

            Result beforeGrow(LevelAccessor level, BlockPos pos, BlockState state);

            EventMapper<Before> EVENT = EventMapper.createUnbound("CropCallback.Grow.Before");
        }

        @FunctionalInterface
        interface After {
            void afterGrow(LevelAccessor level, BlockPos pos, BlockState state);

            EventMapper<After> EVENT = EventMapper.createUnbound("CropCallback.Grow.After");
        }
    }

}
