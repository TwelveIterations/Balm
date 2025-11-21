package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public interface BlockCallback {
    @FunctionalInterface
    interface DigSpeed {
        float computeDigSpeed(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player, float digSpeed);

        EventMapper<DigSpeed> EVENT = EventMapper.createUnbound("BlockCallback.DigSpeed");
    }

    interface Break {
        @FunctionalInterface
        interface Before {
            boolean allowBreak(LevelAccessor level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Player player);

            EventMapper<Before> EVENT = EventMapper.createUnbound("BlockCallback.Break.Before");
        }
    }

    @FunctionalInterface
    interface Use {
        InteractionEventResult handle(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);

        EventMapper<Use> EVENT = EventMapper.createUnbound("BlockCallback.Use");
    }

}

