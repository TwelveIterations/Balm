package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.forge.client.event.ForgeBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "init(II)V", at = @At("HEAD"))
    private void beforeInit(int width, int height, CallbackInfo ci) {
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE.invoker().beforeInit((Screen) (Object) this);
    }

    @Inject(method = "init(II)V", at = @At("TAIL"))
    private void afterInit(int width, int height, CallbackInfo ci) {
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST.invoker().afterInit((Screen) (Object) this);
    }

    @Inject(method = "resize(II)V", at = @At("HEAD"))
    private void beforeResize(int width, int height, CallbackInfo ci) {
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE.invoker().beforeInit((Screen) (Object) this);
    }

    @Inject(method = "resize(II)V", at = @At("TAIL"))
    private void afterResize(int width, int height, CallbackInfo ci) {
        ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST.invoker().afterInit((Screen) (Object) this);
    }

}
