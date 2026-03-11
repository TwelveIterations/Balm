package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(GuiGraphicsExtractor guiGraphics, Font font, int tickCount, int x, int y, ChatComponent.DisplayMode displayMode, boolean changeCursorOnInsertions, CallbackInfo callbackInfo) {
        if (!FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE.invoker().shouldRender(guiGraphics, minecraft.getWindow())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V", at = @At("TAIL"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, Font font, int tickCount, int x, int y, ChatComponent.DisplayMode displayMode, boolean changeCursorOnInsertions, CallbackInfo callbackInfo) {
        FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST.invoker().afterRender(guiGraphics, minecraft.getWindow());
    }
}
