package net.blay09.mods.balm.platform.config.notoml;

import org.jspecify.annotations.Nullable;

public class NotomlStateMachine {
    @Nullable
    private NotomlParserState state = NotomlParserState.None;

    public void transition(NotomlParserState state) {
        this.state = state;
    }

    public void end() {
        state = null;
    }

    public boolean next(NotomlParseBuffer buffer, NotomlTokenConsumer consumer) {
        try {
            if (state != null) {
                state.next(this, buffer, consumer);
            }
        } catch (Exception e) {
            consumer.emitError(new NotomlError(e.getMessage()).at(buffer.getLine()));
            buffer.readUntil("\n");
            state = NotomlParserState.None;
        }
        return state != null;
    }
}
