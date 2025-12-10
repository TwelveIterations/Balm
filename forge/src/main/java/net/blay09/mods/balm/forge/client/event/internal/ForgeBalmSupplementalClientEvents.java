package net.blay09.mods.balm.forge.client.event.internal;

import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.balm.client.platform.event.callback.ScreenCallback;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;

public class ForgeBalmSupplementalClientEvents {
    public static final Event<ClientLifecycleCallback.Started> CLIENT_STARTED = EventFactory.createArrayBacked(ClientLifecycleCallback.Started.class, (listeners) -> (client) -> {
        for (final var listener : listeners) {
            listener.handle(client);
        }
    });

    public static final Event<ScreenCallback.Init.Before> SCREEN_INIT_PRE = EventFactory.createArrayBacked(ScreenCallback.Init.Before.class, (listeners) -> (screen) -> {
        for (final var listener : listeners) {
            listener.beforeInit(screen);
        }
    });

    public static final Event<ScreenCallback.Init.After> SCREEN_INIT_POST = EventFactory.createArrayBacked(ScreenCallback.Init.After.class, (listeners) -> (screen) -> {
        for (final var listener : listeners) {
            listener.afterInit(screen);
        }
    });

    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(guiGraphics, window)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.afterRender(guiGraphics, window);
        }
    });

    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_HEALTH_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(guiGraphics, window)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_HEALTH_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.afterRender(guiGraphics, window);
        }
    });

    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_CHAT_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(guiGraphics, window)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_CHAT_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.afterRender(guiGraphics, window);
        }
    });

    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_BOSS_INFO_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(guiGraphics, window)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_BOSS_INFO_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.afterRender(guiGraphics, window);
        }
    });

    public static final Event<RenderCallback.Gui.Before> RENDER_GUI_PLAYER_LIST_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Before.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(guiGraphics, window)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<RenderCallback.Gui.After> RENDER_GUI_PLAYER_LIST_POST = EventFactory.createArrayBacked(RenderCallback.Gui.After.class, (listeners) -> (guiGraphics, window) -> {
        for (final var listener : listeners) {
            listener.afterRender(guiGraphics, window);
        }
    });
}
