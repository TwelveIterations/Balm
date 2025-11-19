package net.blay09.mods.balm.platform.event.callback;

import com.mojang.brigadier.ParseResults;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.blay09.mods.balm.platform.event.EventHandling;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
public interface CommandCallback {
    EventHandling handle(ParseResults<CommandSourceStack> parseResults);

    EventMapper<CommandCallback> EVENT = EventMapper.createUnbound("CommandCallback");
}
