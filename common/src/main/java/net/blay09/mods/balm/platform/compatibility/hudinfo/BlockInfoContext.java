package net.blay09.mods.balm.platform.compatibility.hudinfo;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public record BlockInfoContext(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, BlockHitResult hitResult, Player player) {
}
