package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.world.entity.BalmForcedPoseHolder;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements BalmForcedPoseHolder {

    @Unique
    @Nullable
    private Pose forcedPose;

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void beforeTick(CallbackInfo callbackInfo) {
        if ((Object) this instanceof ServerPlayer player) {
            FabricBalmSupplementalEvents.SERVER_PLAYER_TICK_PRE.invoker().handle(player);
        }
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    public void afterTick(CallbackInfo callbackInfo) {
        if ((Object) this instanceof ServerPlayer player) {
            FabricBalmSupplementalEvents.SERVER_PLAYER_TICK_POST.invoker().handle(player);
        }
    }

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V"), argsOnly = true)
    private float actuallyHurt(float damageAmount, ServerLevel level, DamageSource damageSource) {
        return FabricBalmSupplementalEvents.LIVING_DAMAGE.invoker().computeDamage((Player) (Object) this, damageSource, damageAmount);
    }

    @Inject(method = "updatePlayerPose()V", at = @At("HEAD"), cancellable = true)
    public void updatePlayerPose(CallbackInfo callbackInfo) {
        if (forcedPose != null) {
            ((Player) (Object) this).setPose(forcedPose);
            callbackInfo.cancel();
        }
    }

    @Override
    @Nullable
    public Pose balm$getForcedPose() {
        return forcedPose;
    }

    @Override
    public void balm$setForcedPose(@Nullable Pose pose) {
        forcedPose = pose;
    }
}
