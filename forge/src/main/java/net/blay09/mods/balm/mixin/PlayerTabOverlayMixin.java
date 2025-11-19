package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(GuiGraphics guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo callbackInfo) {
        GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.PLAYER_LIST);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At("TAIL"))
    public void renderPost(GuiGraphics guiGraphics, int width, Scoreboard scoreboard, Objective objective, CallbackInfo callbackInfo) {
        Balm.getEvents().fireEvent(new GuiDrawEvent.Post(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.PLAYER_LIST));
    }
}
