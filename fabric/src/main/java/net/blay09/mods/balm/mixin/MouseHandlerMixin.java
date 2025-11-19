package net.blay09.mods.balm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @WrapOperation(method = "handleAccumulatedMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z"))
    public boolean mouseDragged(Screen screen, MouseButtonEvent event, double dragX, double dragY, Operation<Boolean> operation) {
        ScreenMouseEvent.Drag.Pre preEvent = new ScreenMouseEvent.Drag.Pre(screen, event.x(), event.y(), event.button(), dragX, dragY);
        Balm.events().fireEvent(preEvent);
        if (preEvent.isCanceled()) {
            return true;
        }

        final var result = operation.call(screen, event, dragX, dragY);
        ScreenMouseEvent.Drag.Post postEvent = new ScreenMouseEvent.Drag.Post(screen, event.x(), event.y(), event.button(), dragX, dragY);
        Balm.events().fireEvent(postEvent);
        return result;
    }

}
