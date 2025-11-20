package net.blay09.mods.balm.client.platform.event;

import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.EventHandling;

public class BalmSupplementalClientEvents {
    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_DEBUG_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_DEBUG_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.handle(guiGraphics, window);
        }
    });

    public static final Event<RenderCallback.BlockHighlight> RENDER_BLOCK_HIGHLIGHT = EventFactory.createArrayBacked(RenderCallback.BlockHighlight.class, (listeners) -> (blockHitResult, poseStack, multiBufferSource, camera) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(blockHitResult, poseStack, multiBufferSource, camera));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });
}
