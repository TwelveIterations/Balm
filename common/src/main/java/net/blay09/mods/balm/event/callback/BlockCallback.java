package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventHandling;
import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface BlockCallback {
    @FunctionalInterface
    interface DigSpeed {
        float handle(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player, float speed);

        EventMapper<DigSpeed> EVENT = EventMapper.createUnbound("BlockCallback.DigSpeed");
    }

    @FunctionalInterface
    interface Break {
        EventHandling handle(LevelAccessor level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Player player);

        EventMapper<Break> EVENT = EventMapper.createUnbound("BlockCallback.Break");
    }

    @FunctionalInterface
    interface Use {
        InteractionResult handle(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);

        EventMapper<Use> EVENT = EventMapper.createUnbound("BlockCallback.Use");
    }

}

