package net.blay09.mods.balm.fabric.internal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.blay09.mods.balm.world.level.block.CustomFarmBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public class FabricCropBlockMixin {

    @WrapOperation(method = "getGrowthSpeed(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValueOrElse(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Comparable;"))
    private static Comparable<?> getGrowthSpeedGetMoisture(BlockState instance, Property<?> property, Comparable<?> comparable, Operation<Comparable<?>> original, Block block, BlockGetter blockGetter, BlockPos pos) {
        final var originalResult = original.call(instance, property, comparable);
        if ((Integer) originalResult <= 0 && instance.getBlock() instanceof CustomFarmBlock customFarmBlock) {
            if (customFarmBlock.canSustainPlant(instance, blockGetter, pos, Direction.UP, block)) {
                if (customFarmBlock.isFertile(instance, blockGetter, pos)) {
                    return 1;
                }
            }
        }

        return originalResult;
    }

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

}

