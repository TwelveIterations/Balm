package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
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
    @Inject(method = "placeNewPlayer", at = @At(value = "RETURN"))
    private void handlePlayerConnection(Connection connection, ServerPlayer player, CommonListenerCookie commonListenerCookie, CallbackInfo callbackInfo) {
        FabricBalmSupplementalEvents.SERVER_PLAYER_LOGIN.invoker().handle(player);
    }
}