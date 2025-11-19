package net.blay09.mods.balm.client.event;

import net.blay09.mods.balm.client.event.callback.RenderCallback;
import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.event.EventHandling;

public class BalmSupplementalClientEvents {
    public static final Event<RenderCallback.Gui.Debug> RENDER_GUI_DEBUG_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Debug.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.Debug> RENDER_GUI_DEBUG_POST = EventFactory.createArrayBacked(RenderCallback.Gui.Debug.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
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
