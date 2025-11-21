package net.blay09.mods.balm.fabric.internal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public ClientLevel level;

    @ModifyVariable(method = "setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = Opcodes.GETFIELD, shift = At.Shift.AFTER), argsOnly = true)
    public Screen modifyScreen(Screen screen) {
        return FabricBalmSupplementalClientEvents.SCREEN_OPEN.invoker().modifyScreen(screen);
    }

    @WrapOperation(method = "startUseItem()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;interactAt(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    public InteractionResult startUseItemInteractAt(MultiPlayerGameMode instance, Player player, Entity entity, EntityHitResult entityHitResult, InteractionHand hand, Operation<InteractionResult> operation) {
        final var result = FabricBalmSupplementalClientEvents.CLIENT_USE_ITEM.invoker().beforeUse(player, hand);
        return result.interactionResult().orElseGet(() -> operation.call(instance, player, entity, entityHitResult, hand));
    }

    @WrapOperation(method = "startUseItem()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"))
    public InteractionResult startUseItemOn(MultiPlayerGameMode instance, LocalPlayer player, InteractionHand hand, BlockHitResult blockHitResult, Operation<InteractionResult> operation) {
        final var result = FabricBalmSupplementalClientEvents.CLIENT_USE_ITEM.invoker().beforeUse(player, hand);
        return result.interactionResult().orElseGet(() -> operation.call(instance, player, hand, blockHitResult));
    }

    @WrapOperation(method = "startUseItem()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    public InteractionResult startUseItem(MultiPlayerGameMode instance, Player player, InteractionHand hand, Operation<InteractionResult> operation) {
        final var result = FabricBalmSupplementalClientEvents.CLIENT_USE_ITEM.invoker().beforeUse(player, hand);
        return result.interactionResult().orElseGet(() -> operation.call(instance, player, hand));
    }

    @Inject(method = "clearClientLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    public void clearClientLevel(Screen p_91321_, CallbackInfo ci) {
        if (this.level != null) {
            FabricBalmSupplementalClientEvents.CLIENT_LEVEL_UNLOAD.invoker().handle(this.level);
        }
    }

    @Inject(method = "setLevel(Lnet/minecraft/client/multiplayer/ClientLevel;)V", at = @At("HEAD"))
    public void setLevel(ClientLevel clientLevel, CallbackInfo ci) {
        if (this.level != null) {
            FabricBalmSupplementalClientEvents.CLIENT_LEVEL_UNLOAD.invoker().handle(this.level);
        }
    }


}
