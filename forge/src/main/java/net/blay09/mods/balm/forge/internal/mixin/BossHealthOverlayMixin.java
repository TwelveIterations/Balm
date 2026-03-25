package net.blay09.mods.balm.forge.internal.mixin;

import net.blay09.mods.balm.forge.client.event.internal.ForgeBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("HEAD"), cancellable = true)
    public void extractRenderStatePre(GuiGraphicsExtractor guiGraphics, CallbackInfo callbackInfo) {
        if (!ForgeBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_PRE.invoker().shouldRender(guiGraphics, minecraft.getWindow())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("TAIL"))
    public void extractRenderStatePost(GuiGraphicsExtractor guiGraphics, CallbackInfo callbackInfo) {
        ForgeBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_POST.invoker().afterRender(guiGraphics, minecraft.getWindow());
    }

}
