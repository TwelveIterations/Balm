package net.blay09.mods.balm.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier(ZF)F", at = @At("TAIL"), cancellable = true)
    private void getFieldOfViewModifier(boolean allowScoping, float current, CallbackInfoReturnable<Float> callbackInfo,
                                        @Local(ordinal = 1) float originalFov) {
        final var effectiveFov = FabricBalmSupplementalClientEvents.UPDATE_FOV.invoker().handle((LivingEntity) (Object) this, originalFov);
        if (effectiveFov != originalFov) {
            callbackInfo.setReturnValue(effectiveFov);
        }
    }

}
