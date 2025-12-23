package net.blay09.mods.balm.fabric.command;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FabricBalmCommands implements BalmCommands {

    private final List<Consumer<CommandDispatcher<CommandSourceStack>>> commands = Collections.synchronizedList(new ArrayList<>());

    public FabricBalmCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> commands.forEach(it -> it.accept(dispatcher)));
    }

    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> initializer) {
        commands.add(initializer);
    }
}
