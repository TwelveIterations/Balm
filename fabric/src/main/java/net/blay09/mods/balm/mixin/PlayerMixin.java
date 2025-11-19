package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.entity.BalmPlayer;
import net.blay09.mods.balm.fabric.event.FabricBalmSupplementalEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin implements BalmPlayer {

    @Unique
    private Pose forcedPose;

    @ModifyVariable(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V"), argsOnly = true)
    private float actuallyHurt(float damageAmount, ServerLevel level, DamageSource damageSource) {
        return FabricBalmSupplementalEvents.LIVING_DAMAGE.invoker().handle((Player) (Object) this, damageSource, damageAmount);
    }

    @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void attack(Entity entity, CallbackInfo callbackInfo) {
        Player player = (Player) (Object) this;
        if (FabricBalmSupplementalEvents.PLAYER_ATTACK.invoker()
                .handle(player, entity)
                .shouldSkipDefault()) {
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
    public Pose balm$getForcedPose() {
        return forcedPose;
    }

    @Override
    public void balm$setForcedPose(Pose pose) {
        forcedPose = pose;
    }
}
