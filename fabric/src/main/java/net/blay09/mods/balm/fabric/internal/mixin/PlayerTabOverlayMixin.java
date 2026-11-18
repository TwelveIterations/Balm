package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("HEAD"), cancellable = true)
    public void extractRenderStatePre(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (!FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE.invoker().shouldRender(graphics, minecraft.getWindow())) {
            ci.cancel();
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V", at = @At("TAIL"))
    public void extractRenderStatePost(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST.invoker().afterRender(graphics, minecraft.getWindow());
    }
}
