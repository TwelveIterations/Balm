package net.blay09.mods.balm.platform.event.callback;

import com.mojang.brigadier.ParseResults;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.commands.CommandSourceStack;

public interface CommandCallback {
    @FunctionalInterface
    interface Before {
        boolean allowCommand(ParseResults<CommandSourceStack> parseResults);

        EventMapper<Before> EVENT = EventMapper.createUnbound("CommandCallback.Before");
    }
}
