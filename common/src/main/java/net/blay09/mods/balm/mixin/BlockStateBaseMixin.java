package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {

    @Inject(method = "getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", at = @At("RETURN"), cancellable = true)
    public void getDestroyProgress(Player player, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        final var blockStateBase = (BlockBehaviour.BlockStateBase) (Object) this;
        final var digSpeed = cir.getReturnValueF();
        final var event = new DigSpeedEvent(blockGetter, blockPos, ((BlockStateBaseAccessor) blockStateBase).callAsState(), digSpeed, player);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            cir.setReturnValue(-1f);
        } else if (event.getSpeedOverride() != null) {
            cir.setReturnValue(event.getSpeedOverride());
        }
    }
}