package net.blay09.mods.balm.neoforge.internal.mixin;

import net.blay09.mods.balm.neoforge.client.platform.event.internal.NeoForgeBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "run()V", at = @At("HEAD"))
    void run(CallbackInfo callbackInfo) {
        NeoForgeBalmSupplementalClientEvents.CLIENT_STARTED.invoker().handle(Minecraft.getInstance());
    }
}
