package net.blay09.mods.balm.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.suggestions.SuggestionContext;

import java.util.function.Consumer;

public interface BalmClientCommands {
    void register(Consumer<CommandDispatcher<SuggestionContext>> initializer);
}
