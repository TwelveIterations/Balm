package net.blay09.mods.balm.fabric.internal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier(ZF)F", at = @At("TAIL"), cancellable = true)
    private void getFieldOfViewModifier(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> callbackInfo,
                                        @Local(name = "modifier") float modifier) {
        final var effectiveFov = FabricBalmSupplementalClientEvents.UPDATE_FOV.invoker().computeFov((LivingEntity) (Object) this, modifier);
        if (effectiveFov != modifier) {
            callbackInfo.setReturnValue(Mth.lerp(effectScale, 1f, effectiveFov));
        }
    }

}
