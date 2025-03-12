package net.blay09.mods.balm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.LivingFallEvent;
import net.blay09.mods.balm.api.event.LivingHealEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"), argsOnly = true)
    private float actuallyHurt(float damageAmount, ServerLevel serverLevel, DamageSource damageSource) {
        LivingDamageEvent event = new LivingDamageEvent((LivingEntity) (Object) this, damageSource, damageAmount);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            return 0f;
        } else {
            return event.getDamageAmount();
        }
    }

    @Inject(method = "causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
    private void causeFallDamage(double distance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo, @Share("eventRef") LocalRef<LivingFallEvent> eventRef) {
        LivingFallEvent event = new LivingFallEvent((LivingEntity) (Object) this);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.setReturnValue(false);
        }
        eventRef.set(event);
    }

    @WrapOperation(method = "causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
    private int calculateFallDamage(LivingEntity self, double fallDistance, float multiplier, Operation<Integer> operation, @Share("eventRef") LocalRef<LivingFallEvent> eventRef) {
        LivingFallEvent event = eventRef.get();
        if (event != null && event.getFallDamageOverride() != null) {
            return event.getFallDamageOverride().intValue();
        }
        return operation.call(self, fallDistance, multiplier);
    }

    @ModifyVariable(method = "heal(F)V", at = @At("HEAD"), argsOnly = true)
    private float modifyHealing(float heal) {
        LivingEntity entity = (LivingEntity) (Object) this;
        LivingHealEvent event = new LivingHealEvent(entity, heal);
        return event.isCanceled() ? 0f : heal;
    }

}
