package net.blay09.mods.balm.neoforge.client.internal.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.client.commands.BalmClientCommands;
import net.minecraft.commands.suggestions.SuggestionContext;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NeoForgeBalmClientCommands implements BalmClientCommands {

    private final List<Consumer<CommandDispatcher<SuggestionContext>>> commands = Collections.synchronizedList(new ArrayList<>());

    public NeoForgeBalmClientCommands() {
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> {
            final var sharedDispatcher = asSharedDispatcher(event.getDispatcher());
            commands.forEach(it -> it.accept(sharedDispatcher));
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static CommandDispatcher<SuggestionContext> asSharedDispatcher(CommandDispatcher<?> dispatcher) {
        return (CommandDispatcher) dispatcher;
    }

    @Override
    public void register(Consumer<CommandDispatcher<SuggestionContext>> initializer) {
        commands.add(initializer);
    }
}
