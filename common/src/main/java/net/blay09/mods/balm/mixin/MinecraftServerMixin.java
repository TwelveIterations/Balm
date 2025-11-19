package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.blay09.mods.balm.event.BalmSupplementalEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;", at = @At("RETURN"), cancellable = true)
    private void reloadResources(Collection<String> p_129862_, CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) {
        callbackInfo.getReturnValue().thenAccept(it -> Balm.events().fireEvent(new ServerReloadFinishedEvent((MinecraftServer) (Object) this)));
        callbackInfo.getReturnValue().thenAccept(it -> BalmSupplementalEvents.SERVER_RELOADED.invoker().handle((MinecraftServer) (Object) this));
    }

}
