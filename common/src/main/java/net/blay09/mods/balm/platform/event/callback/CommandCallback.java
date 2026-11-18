package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.commands.ParseBox;

public interface CommandCallback {
    @FunctionalInterface
    interface Before {
        boolean allowCommand(ParseBox.Result command);

        EventMapper<Before> EVENT = EventMapper.createUnbound("CommandCallback.Before");
    }
}
