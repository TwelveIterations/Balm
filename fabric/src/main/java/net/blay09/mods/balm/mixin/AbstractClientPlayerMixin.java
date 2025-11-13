package net.blay09.mods.balm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.FovUpdateEvent;
import net.blay09.mods.balm.fabric.client.event.FabricBalmSupplementalClientEvents;
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
        FovUpdateEvent event = new FovUpdateEvent((LivingEntity) (Object) this, originalFov);
        Balm.getEvents().fireEvent(event);
        float effectiveFov = event.getFov() != null ? event.getFov() : originalFov;
        effectiveFov = FabricBalmSupplementalClientEvents.UPDATE_FOV.invoker().handle((LivingEntity) (Object) this, effectiveFov);
        if (effectiveFov != originalFov) {
            callbackInfo.setReturnValue(effectiveFov);
        }
    }

}
