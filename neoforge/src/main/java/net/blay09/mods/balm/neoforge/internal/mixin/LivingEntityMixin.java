package net.blay09.mods.balm.neoforge.internal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.neoforge.platform.event.internal.NeoForgeBalmSupplementalEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z", ordinal = 1))
    private boolean hurtServerIsDeadOrDying(LivingEntity instance, Operation<Boolean> original, ServerLevel level, DamageSource damageSource, float damageAmount) {
        return original.call(instance) && NeoForgeBalmSupplementalEvents.BEFORE_DEATH.invoker().allowDeath(instance, damageSource, damageAmount);
    }
}
