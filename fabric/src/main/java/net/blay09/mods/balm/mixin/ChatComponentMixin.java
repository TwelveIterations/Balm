package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
    Minecraft minecraft;

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(GuiGraphics guiGraphics, Font font, int tickCount, int x, int y, boolean bl, boolean ble, CallbackInfo callbackInfo) {
        GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.CHAT);
        Balm.events().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;IIIZZ)V", at = @At("TAIL"))
    public void renderPost(GuiGraphics guiGraphics, Font font, int tickCount, int x, int y, boolean bl, boolean ble, CallbackInfo callbackInfo) {
        Balm.events().fireEvent(new GuiDrawEvent.Post(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.CHAT));
    }
}
