package net.blay09.mods.balm.fabric.internal.mixin;

import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.ParseBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class CommandsMixin {
    @Inject(method = "performCommand(Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/commands/ParseBox$Result;Ljava/lang/String;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/commands/Commands;executeCommandInContext(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/function/Consumer;)V"), cancellable = true)
    private static void onCommandExecuted(CommandSourceStack executionContext, ParseBox.Result command, String commandString, CallbackInfo ci) {
        if (!FabricBalmSupplementalEvents.COMMAND.invoker().allowCommand(command)) {
            ci.cancel();
        }
    }
}
