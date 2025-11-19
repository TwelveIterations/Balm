package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.world.level.block.CustomFarmBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {

    @Inject(method = "mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    public void mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (state.getBlock() instanceof CustomFarmBlock customFarmBlock) {
            callbackInfo.setReturnValue(customFarmBlock.canSustainPlant(state,
                    blockGetter,
                    pos,
                    Direction.UP,
                    blockGetter.getBlockState(pos.relative(Direction.UP)).getBlock()));
        }
    }

}
