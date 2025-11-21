package net.blay09.mods.balm.fabric.internal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StemBlock.class)
public class FabricStemBlockMixin {
    @WrapOperation(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private int wrapRandomNextInt(RandomSource randomSource, int bound, Operation<Integer> original, BlockState state, ServerLevel level, BlockPos pos) {
        final var result = FabricBalmSupplementalEvents.CROP_GROW_PRE.invoker().beforeGrow(level, pos, state);
        return switch (result) {
            case DO_NOT_GROW -> -1;
            case GROW -> 0;
            default -> original.call(randomSource, bound);
        };
    }

    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.AFTER))
    public void randomTickPostGrow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo callbackInfo) {
        FabricBalmSupplementalEvents.CROP_GROW_POST.invoker().afterGrow(level, pos, state);
    }

    @Inject(method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", shift = At.Shift.AFTER, ordinal = 1))
    public void randomTickPostGrow2(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo callbackInfo) {
        FabricBalmSupplementalEvents.CROP_GROW_POST.invoker().afterGrow(level, pos, state);
    }

}
