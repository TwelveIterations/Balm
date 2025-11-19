package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.blay09.mods.balm.client.event.BalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        final var window = minecraft.getWindow();
        GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(window, guiGraphics, GuiDrawEvent.Element.DEBUG);
        Balm.events().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        } else {
            if (BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE.invoker()
                    .handle(guiGraphics, window)
                    .shouldSkipDefault()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("TAIL"))
    public void renderPost(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        final var window = minecraft.getWindow();
        Balm.events().fireEvent(new GuiDrawEvent.Post(window, guiGraphics, GuiDrawEvent.Element.DEBUG));
        BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST.invoker().handle(guiGraphics, window);
    }
}
