package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.BOSS_INFO);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("TAIL"))
    public void renderPost(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        Balm.getEvents().fireEvent(new GuiDrawEvent.Post(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.BOSS_INFO));
    }

}
