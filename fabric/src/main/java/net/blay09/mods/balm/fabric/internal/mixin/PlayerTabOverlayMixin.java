package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
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

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At("HEAD"), cancellable = true)
    public void extractRenderStatePre(GuiGraphicsExtractor guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo callbackInfo) {
        if (!FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE.invoker().shouldRender(guiGraphics, minecraft.getWindow())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At("TAIL"))
    public void extractRenderStatePost(GuiGraphicsExtractor guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo callbackInfo) {
        FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST.invoker().afterRender(guiGraphics, minecraft.getWindow());
    }
}
