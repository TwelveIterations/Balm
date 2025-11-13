package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.blay09.mods.balm.forge.client.event.ForgeBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("HEAD"))
    private void beforeInit(Minecraft client, int width, int height, CallbackInfo ci) {
        Balm.getEvents().fireEvent(new ScreenInitEvent.Pre((Screen) (Object) this));
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE.invoker().handle((Screen) (Object) this);
    }

    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("TAIL"))
    private void afterInit(Minecraft client, int width, int height, CallbackInfo ci) {
        Balm.getEvents().fireEvent(new ScreenInitEvent.Post((Screen) (Object) this));
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST.invoker().handle((Screen) (Object) this);
    }

    @Inject(method = "resize(Lnet/minecraft/client/Minecraft;II)V", at = @At("HEAD"))
    private void beforeResize(Minecraft client, int width, int height, CallbackInfo ci) {
        Balm.getEvents().fireEvent(new ScreenInitEvent.Pre((Screen) (Object) this));
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE.invoker().handle((Screen) (Object) this);
    }

    @Inject(method = "resize(Lnet/minecraft/client/Minecraft;II)V", at = @At("TAIL"))
    private void afterResize(Minecraft client, int width, int height, CallbackInfo ci) {
        Balm.getEvents().fireEvent(new ScreenInitEvent.Post((Screen) (Object) this));
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST.invoker().handle((Screen) (Object) this);
    }

}
