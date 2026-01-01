package net.blay09.mods.balm.fabric.internal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.stream.Collectors;

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

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void addEffect(MobEffectInstance effectInstance, Entity source, CallbackInfoReturnable<Boolean> cir) {
        final var entity = (LivingEntity) (Object) this;
        if (!FabricBalmSupplementalEvents.MOB_EFFECT_APPLY.invoker().allowApply(entity, effectInstance, source)) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object addEffect(Map<Holder<MobEffect>, MobEffectInstance> instance, Object effect, Operation<MobEffectInstance> original, MobEffectInstance effectInstance, @Nullable Entity source) {
        final var previousEffectInstance = original.call(instance, effect);
        FabricBalmSupplementalEvents.MOB_EFFECT_ADD.invoker().effectAdded((LivingEntity) (Object) this, effectInstance, previousEffectInstance, source);
        return previousEffectInstance;
    }

    @Inject(method = "removeEffect(Lnet/minecraft/core/Holder;)Z", at = @At("HEAD"), cancellable = true)
    private void removeEffect(Holder<MobEffect> effect, CallbackInfoReturnable<Boolean> cir) {
        final var entity = (LivingEntity) (Object) this;
        final var effectInstance = entity.getEffect(effect);
        if (!FabricBalmSupplementalEvents.MOB_EFFECT_REMOVE.invoker().allowRemove(entity, effect, effectInstance)) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "removeAllEffects()Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V"))
    private void clearAllEffects(Map<Holder<MobEffect>, MobEffectInstance> activeEffects, Operation<Void> original, @Local Map<Holder<MobEffect>, MobEffectInstance> map) {
        final var entity = (LivingEntity) (Object) this;
        final var effectsToRetain = activeEffects.entrySet().stream()
                .filter(it -> !FabricBalmSupplementalEvents.MOB_EFFECT_REMOVE.invoker().allowRemove(entity, it.getKey(), it.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        original.call(activeEffects);
        activeEffects.putAll(effectsToRetain);
        effectsToRetain.forEach((effect, effectInstance) -> map.remove(effect));
    }

    @WrapOperation(method = "tickEffects()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/lang/Runnable;)Z"))
    private boolean tickServer(MobEffectInstance effectInstance, ServerLevel level, LivingEntity entity, Runnable runnable, Operation<Boolean> original) {
        final var originalResult = original.call(effectInstance, level, entity, runnable);
        if (!originalResult) {
            if (!FabricBalmSupplementalEvents.MOB_EFFECT_EXPIRE.invoker().allowExpire(entity, effectInstance)) {
                return true;
            }
        }
        return originalResult;
    }

}
