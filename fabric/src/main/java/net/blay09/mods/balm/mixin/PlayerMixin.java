package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.entity.BalmPlayer;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.PlayerAttackEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin implements BalmPlayer {

    private Pose forcedPose;

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V"), argsOnly = true)
    private float actuallyHurt(float damageAmount, ServerLevel level, DamageSource damageSource) {
        LivingDamageEvent event = new LivingDamageEvent((Player) (Object) this, damageSource, damageAmount);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            return 0f;
        } else {
            return event.getDamageAmount();
        }
    }

    @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void attack(Entity entity, CallbackInfo callbackInfo) {
        Player player = (Player) (Object) this;
        PlayerAttackEvent event = new PlayerAttackEvent(player, entity);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "updatePlayerPose()V", at = @At("HEAD"), cancellable = true)
    public void updatePlayerPose(CallbackInfo callbackInfo) {
        if (forcedPose != null) {
            ((Player) (Object) this).setPose(forcedPose);
            callbackInfo.cancel();
        }
    }

    @Override
    public Pose getForcedPose() {
        return forcedPose;
    }

    @Override
    public void setForcedPose(Pose pose) {
        forcedPose = pose;
    }
}
