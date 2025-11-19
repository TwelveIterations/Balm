package net.blay09.mods.balm.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"), argsOnly = true)
    private float actuallyHurt(float damageAmount, ServerLevel serverLevel, DamageSource damageSource) {
        return FabricBalmSupplementalEvents.LIVING_DAMAGE.invoker().handle((LivingEntity) (Object) this, damageSource, damageAmount);
    }

    @WrapOperation(method = "causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(DF)I"))
    private int calculateFallDamage(LivingEntity self, double fallDistance, float multiplier, Operation<Integer> operation) {
        float effectiveDamage = operation.call(self, fallDistance, multiplier);
        effectiveDamage = FabricBalmSupplementalEvents.LIVING_FALL.invoker().handle(self, effectiveDamage);
        return Mth.floor(effectiveDamage);
    }

    @ModifyVariable(method = "heal(F)V", at = @At("HEAD"), argsOnly = true)
    private float modifyHealing(float heal) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return FabricBalmSupplementalEvents.LIVING_HEAL.invoker().handle(entity, heal);
    }

}
