package net.blay09.mods.balm.forge.internal.mixin;

import net.blay09.mods.balm.forge.client.event.internal.ForgeBalmSupplementalClientEvents;
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
    public void extractRenderStatePre(GuiGraphicsExtractor graphics, Font par2, int par3, int par4, int par5, ChatComponent.DisplayMode par6, boolean par7, CallbackInfo ci) {
            if (!ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE.invoker().shouldRender(graphics, minecraft.getWindow())) {
                ci.cancel();
            }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;IIILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;Z)V", at = @At("TAIL"))
    public void extractRenderStatePost(GuiGraphicsExtractor graphics, Font par2, int par3, int par4, int par5, ChatComponent.DisplayMode par6, boolean par7, CallbackInfo ci) {
        ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST.invoker().afterRender(graphics, minecraft.getWindow());
    }
}
