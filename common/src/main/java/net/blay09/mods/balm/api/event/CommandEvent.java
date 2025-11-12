package net.blay09.mods.balm.api.event;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.CommandCallback} instead.
 */
@Deprecated
public class CommandEvent extends BalmEvent {
    private final ParseResults<CommandSourceStack> parseResults;

    public CommandEvent(ParseResults<CommandSourceStack> parseResults) {
        this.parseResults = parseResults;
    }

    public ParseResults<CommandSourceStack> getParseResults() {
        return parseResults;
    }
}
