package net.blay09.mods.balm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @WrapOperation(method = "handleAccumulatedMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(DDIDD)Z"))
    public boolean mouseDragged(Screen screen, double mouseX, double mouseY, int activeButton, double dragX, double dragY, Operation<Boolean> operation) {
        ScreenMouseEvent.Drag.Pre preEvent = new ScreenMouseEvent.Drag.Pre(screen, mouseX, mouseY, activeButton, dragX, dragY);
        Balm.getEvents().fireEvent(preEvent);
        if (preEvent.isCanceled()) {
            return true;
        }

        final var result = operation.call(screen, mouseX, mouseY, activeButton, dragX, dragY);
        ScreenMouseEvent.Drag.Post postEvent = new ScreenMouseEvent.Drag.Post(screen, mouseX, mouseY, activeButton, dragX, dragY);
        Balm.getEvents().fireEvent(postEvent);
        return result;
    }

}
