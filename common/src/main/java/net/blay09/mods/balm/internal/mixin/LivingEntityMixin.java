package net.blay09.mods.balm.internal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.platform.event.internal.BalmSupplementalEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(method = "causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(DF)I"))
    private int calculateFallDamage(LivingEntity self, double fallDistance, float multiplier, Operation<Integer> operation) {
        float effectiveDamage = operation.call(self, fallDistance, multiplier);
        effectiveDamage = BalmSupplementalEvents.LIVING_FALL.invoker().computeFallDamage(self, effectiveDamage);
        return Mth.floor(effectiveDamage);
    }
}
