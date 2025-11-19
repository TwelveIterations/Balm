package net.blay09.mods.balm.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.PlayerChangedDimensionEvent;
import net.blay09.mods.balm.api.event.PlayerOpenMenuEvent;
import net.blay09.mods.balm.api.event.TossItemEvent;
import net.blay09.mods.balm.fabric.event.FabricBalmSupplementalEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;", at = @At("RETURN"))
    public void openMenu(@Nullable MenuProvider menuProvider, CallbackInfoReturnable<OptionalInt> callbackInfo) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        Balm.events().fireEvent(new PlayerOpenMenuEvent(player, player.containerMenu));
        FabricBalmSupplementalEvents.SERVER_PLAYER_OPEN_MENU.invoker().handle(player, player.containerMenu);
    }

    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("HEAD"))
    public void teleportHead(TeleportTransition transition, CallbackInfoReturnable<ServerPlayer> callbackInfo, @Share("fromDimHolder") LocalRef<ResourceKey<Level>> fromDimHolder) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        fromDimHolder.set(player.level().dimension());
    }

    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("RETURN"))
    public void teleportTail(TeleportTransition transition, CallbackInfoReturnable<ServerPlayer> callbackInfo, @Share("fromDimHolder") LocalRef<ResourceKey<Level>> fromDimHolder) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        final ResourceKey<Level> fromDim = fromDimHolder.get();
        final ResourceKey<Level> toDim = transition.newLevel().dimension();
        if (!fromDim.equals(toDim)) {
            Balm.events().fireEvent(new PlayerChangedDimensionEvent(player, fromDim, toDim));
            FabricBalmSupplementalEvents.SERVER_PLAYER_CHANGED_DIMENSION.invoker().handle(player, fromDim, toDim);
        }
    }

    @Inject(method = "drop(Z)V", at = @At("HEAD"), cancellable = true)
    public void drop(boolean flag, CallbackInfo callbackInfo) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        Inventory inventory = player.getInventory();
        ItemStack selected = inventory.getSelectedItem();
        TossItemEvent event = new TossItemEvent(player, selected);
        Balm.events().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        } else if (FabricBalmSupplementalEvents.ITEM_TOSSED.invoker()
                .handle(player, selected)
                .shouldSkipDefault()) {
            callbackInfo.cancel();
        }
    }
}
