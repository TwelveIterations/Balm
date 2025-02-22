package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {
    @Inject(method = "repositionElements", at = @At("HEAD"))
    private void repositionElements(CallbackInfo ci) {
        Balm.getEvents().fireEvent(new ScreenInitEvent.Pre((Screen) (Object) this));
    }

    @Inject(method = "repositionElements", at = @At("RETURN"))
    private void repositionElementsPost(CallbackInfo ci) {
        Balm.getEvents().fireEvent(new ScreenInitEvent.Post((Screen) (Object) this));
    }

}
