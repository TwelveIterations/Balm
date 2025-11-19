package net.blay09.mods.balm.fabric.mixin;

import net.blay09.mods.balm.fabric.client.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    public void renderAllPre(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        if (FabricBalmSupplementalClientEvents.RENDER_GUI_PRE.invoker()
                .handle(guiGraphics, minecraft.getWindow())
                .shouldSkipDefault()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("TAIL"))
    public void renderAllPost(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        FabricBalmSupplementalClientEvents.RENDER_GUI_POST.invoker().handle(guiGraphics, minecraft.getWindow());
    }

    @Inject(method = "renderPlayerHealth(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true)
    public void renderPlayerHealthPre(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        if (FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE.invoker()
                .handle(guiGraphics, minecraft.getWindow())
                .shouldSkipDefault()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderPlayerHealth(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("TAIL"))
    public void renderPlayerHealthPost(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST.invoker().handle(guiGraphics, minecraft.getWindow());
    }
}
