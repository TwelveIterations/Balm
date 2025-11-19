package net.blay09.mods.balm.neoforge.internal.mixin;

import net.blay09.mods.balm.neoforge.platform.event.internal.NeoForgeBalmSupplementalEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerAbilitiesPacket;<init>(Lnet/minecraft/world/entity/player/Abilities;)V"))
    private void handlePlayerConnection(Connection connection, ServerPlayer player, CommonListenerCookie commonListenerCookie, CallbackInfo callbackInfo) {
        NeoForgeBalmSupplementalEvents.SERVER_PLAYER_CONNECTED.invoker().handle(player);
    }
}