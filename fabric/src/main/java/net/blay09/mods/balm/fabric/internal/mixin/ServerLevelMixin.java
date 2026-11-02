package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;commonTick()V"))
    private void tickNonPassengerBefore(Entity entity, CallbackInfo callbackInfo) {
        FabricBalmSupplementalEvents.SERVER_ENTITY_TICK_PRE.invoker().handle(entity);
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
    private void tickNonPassengerAfter(Entity entity, CallbackInfo callbackInfo) {
        FabricBalmSupplementalEvents.SERVER_ENTITY_TICK_POST.invoker().handle(entity);
    }
}
