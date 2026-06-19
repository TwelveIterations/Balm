package net.blay09.mods.balm.fabric.client.internal.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.api.client.commands.BalmClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FabricBalmClientCommands implements BalmClientCommands {

    private final List<Consumer<CommandDispatcher<SharedSuggestionProvider>>> commands = Collections.synchronizedList(new ArrayList<>());

    public FabricBalmClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            final var sharedDispatcher = asSharedDispatcher(dispatcher);
            commands.forEach(it -> it.accept(sharedDispatcher));
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static CommandDispatcher<SharedSuggestionProvider> asSharedDispatcher(CommandDispatcher<?> dispatcher) {
        return (CommandDispatcher) dispatcher;
    }

    @Override
    public void register(Consumer<CommandDispatcher<SharedSuggestionProvider>> initializer) {
        commands.add(initializer);
    }
}
