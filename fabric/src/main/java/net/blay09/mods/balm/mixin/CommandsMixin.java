package net.blay09.mods.balm.mixin;

import com.mojang.brigadier.ParseResults;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.CommandEvent;
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
        final var event = new CommandEvent(parseResults);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
