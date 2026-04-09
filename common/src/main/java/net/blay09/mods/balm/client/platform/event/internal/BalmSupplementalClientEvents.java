package net.blay09.mods.balm.client.platform.event.internal;

import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;

public class BalmSupplementalClientEvents {
    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_DEBUG_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(guiGraphics, window)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_DEBUG_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.afterRender(guiGraphics, window);
        }
    });

    public static final Event<RenderCallback.BlockHighlight> RENDER_BLOCK_HIGHLIGHT = EventFactory.createArrayBacked(RenderCallback.BlockHighlight.class, (listeners) -> (blockHitResult, camera, levelRenderState) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(blockHitResult, camera, levelRenderState)) {
                return false;
            }
        }
        return true;
    });
}
