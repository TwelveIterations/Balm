package net.blay09.mods.balm.fabric.internal.mixin;

import com.mojang.brigadier.ParseResults;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class CommandsMixin {
    @Inject(method = "performCommand(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/commands/Commands;executeCommandInContext(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/function/Consumer;)V"), cancellable = true)
    private void onCommandExecuted(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
        if (!FabricBalmSupplementalEvents.COMMAND.invoker().allowCommand(parseResults)) {
            ci.cancel();
        }
    }
}
