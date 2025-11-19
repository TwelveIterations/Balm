package net.blay09.mods.balm.neoforge.internal.mixin;

import net.blay09.mods.balm.neoforge.client.platform.event.internal.NeoForgeBalmSupplementalClientEvents;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "init(II)V", at = @At("HEAD"))
    private void beforeInit(int width, int height, CallbackInfo ci) {
        NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE.invoker().handle((Screen) (Object) this);
    }

    @Inject(method = "init(II)V", at = @At("TAIL"))
    private void afterInit(int width, int height, CallbackInfo ci) {
        NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_POST.invoker().handle((Screen) (Object) this);
    }

    @Inject(method = "resize(II)V", at = @At("HEAD"))
    private void beforeResize(int width, int height, CallbackInfo ci) {
        NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE.invoker().handle((Screen) (Object) this);
    }

    @Inject(method = "resize(II)V", at = @At("TAIL"))
    private void afterResize(int width, int height, CallbackInfo ci) {
        NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_POST.invoker().handle((Screen) (Object) this);
    }

}
