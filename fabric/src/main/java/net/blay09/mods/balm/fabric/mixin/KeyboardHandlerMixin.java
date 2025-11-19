package net.blay09.mods.balm.fabric.mixin;

import net.blay09.mods.balm.fabric.client.event.FabricBalmSupplementalClientEvents;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V", at = @At("TAIL"))
    public void keyPress(long window, int action, KeyEvent event, CallbackInfo callbackInfo) {
        if (window == this.minecraft.getWindow().handle()) {
            FabricBalmSupplementalClientEvents.KEYBOARD_INPUT.invoker().handle(event.key(), event.scancode(), action, event.modifiers());
        }
    }

}
