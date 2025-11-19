package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ContainerScreenDrawEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "RETURN"))
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        Screen screen = (Screen) (Object) this;
        Balm.getEvents().fireEvent(new ContainerScreenDrawEvent.Background(screen, guiGraphics, mouseX, mouseY));
    }

    @Inject(method = "renderContents(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V", shift = At.Shift.AFTER))
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        Screen screen = (Screen) (Object) this;
        Balm.getEvents().fireEvent(new ContainerScreenDrawEvent.Foreground(screen, guiGraphics, mouseX, mouseY));
    }

}
