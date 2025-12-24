package net.blay09.mods.balm.forge.commands.internal;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.commands.BalmCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ForgeBalmCommands implements BalmCommands {

    private final List<Consumer<CommandDispatcher<CommandSourceStack>>> commands = Collections.synchronizedList(new ArrayList<>());

    public ForgeBalmCommands() {
        RegisterCommandsEvent.BUS.addListener((event) -> {
            commands.forEach(it -> it.accept(event.getDispatcher()));
        });
    }

    @Override
    public void register(Consumer<CommandDispatcher<CommandSourceStack>> initializer) {
        commands.add(initializer);
    }
}
