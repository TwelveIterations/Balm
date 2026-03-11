package net.blay09.mods.balm.internal.mixin;

import net.blay09.mods.balm.client.platform.event.internal.BalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("HEAD"), cancellable = true)
    public void extractRenderStatePre(GuiGraphicsExtractor guiGraphics, CallbackInfo callbackInfo) {
        final var window = minecraft.getWindow();
        if (!BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE.invoker()
                .shouldRender(guiGraphics, window)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("TAIL"))
    public void extractRenderStatePost(GuiGraphicsExtractor guiGraphics, CallbackInfo callbackInfo) {
        final var window = minecraft.getWindow();
        BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST.invoker().afterRender(guiGraphics, window);
    }
}
