package net.blay09.mods.balm.neoforge.command;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NeoForgeBalmCommands implements BalmCommands {

    private final List<Consumer<CommandDispatcher<CommandSourceStack>>> commands = Collections.synchronizedList(new ArrayList<>());

    public NeoForgeBalmCommands() {
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            commands.forEach(it -> it.accept(event.getDispatcher()));
        });
    }

    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> initializer) {
        commands.add(initializer);
    }
}
