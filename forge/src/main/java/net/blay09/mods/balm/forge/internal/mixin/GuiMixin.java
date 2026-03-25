package net.blay09.mods.balm.forge.internal.mixin;

import net.blay09.mods.balm.forge.client.event.internal.ForgeBalmSupplementalClientEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    public void extractRenderStatePre(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        if (!ForgeBalmSupplementalClientEvents.RENDER_GUI_PRE.invoker().shouldRender(guiGraphics, minecraft.getWindow())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V", at = @At("TAIL"))
    public void extractRenderStatePost(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        ForgeBalmSupplementalClientEvents.RENDER_GUI_POST.invoker().afterRender(guiGraphics, minecraft.getWindow());
    }

    @Inject(method = "extractPlayerHealth(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("HEAD"), cancellable = true)
    public void extractPlayerHealthPre(GuiGraphicsExtractor guiGraphics, CallbackInfo callbackInfo) {
        if (!ForgeBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE.invoker().shouldRender(guiGraphics, minecraft.getWindow())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "extractPlayerHealth(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("TAIL"))
    public void extractPlayerHealthPost(GuiGraphicsExtractor guiGraphics, CallbackInfo callbackInfo) {
        ForgeBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST.invoker().afterRender(guiGraphics, minecraft.getWindow());
    }
}
