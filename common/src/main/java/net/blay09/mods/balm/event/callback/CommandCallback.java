package net.blay09.mods.balm.event.callback;

import com.mojang.brigadier.ParseResults;
import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface CommandCallback {
    void handle(ParseResults<CommandSourceStack> parseResults);

    EventMapper<CommandCallback> EVENT = EventMapper.createUnbound();
}
