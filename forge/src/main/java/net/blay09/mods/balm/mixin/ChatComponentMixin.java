package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.blay09.mods.balm.forge.client.event.ForgeBalmSupplementalClientEvents;
import net.minecraft.client.Minecraft;
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
    private Minecraft minecraft;

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIZ)V", at = @At("HEAD"), cancellable = true)
    public void renderPre(GuiGraphics guiGraphics, int tickCount, int x, int y, boolean bl, CallbackInfo callbackInfo) {
        GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.CHAT);
        Balm.events().fireEvent(event);
        if (event.isCanceled()) {
            callbackInfo.cancel();
        } else {
            if (ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE.invoker()
                    .handle(guiGraphics, minecraft.getWindow())
                    .shouldSkipDefault()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIIZ)V", at = @At("TAIL"))
    public void renderPost(GuiGraphics guiGraphics, int tickCount, int x, int y, boolean bl, CallbackInfo callbackInfo) {
        Balm.events().fireEvent(new GuiDrawEvent.Post(minecraft.getWindow(), guiGraphics, GuiDrawEvent.Element.CHAT));
        ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST.invoker().handle(guiGraphics, minecraft.getWindow());
    }
}
