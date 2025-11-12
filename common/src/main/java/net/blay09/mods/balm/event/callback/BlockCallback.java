package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface BlockCallback {
    @FunctionalInterface
    interface DigSpeed {
        float handle(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player, float speed);

        EventMapper<DigSpeed> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Break {
        void handle(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable Player player);

        EventMapper<Break> BREAK = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Use {
        void handle(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);

        EventMapper<Use> USE = EventMapper.createUnbound();
    }

}

