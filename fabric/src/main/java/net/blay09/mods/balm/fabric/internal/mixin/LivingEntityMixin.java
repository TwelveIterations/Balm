package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"), argsOnly = true)
    private float actuallyHurt(float damageAmount, ServerLevel serverLevel, DamageSource damageSource) {
        return FabricBalmSupplementalEvents.LIVING_DAMAGE.invoker().computeDamage((LivingEntity) (Object) this, damageSource, damageAmount);
    }

    @ModifyVariable(method = "heal(F)V", at = @At("HEAD"), argsOnly = true)
    private float modifyHealing(float heal) {
        LivingEntity entity = (LivingEntity) (Object) this;
        return FabricBalmSupplementalEvents.LIVING_HEAL.invoker().computeHeal(entity, heal);
    }

}
