package net.blay09.mods.balm.forge.client.event;

import net.blay09.mods.balm.client.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.client.event.callback.RenderCallback;
import net.blay09.mods.balm.client.event.callback.ScreenCallback;
import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.event.EventHandling;

public class ForgeBalmSupplementalClientEvents {
    public static final Event<ClientLifecycleCallback> CLIENT_STARTED = EventFactory.createArrayBacked(ClientLifecycleCallback.class, (listeners) -> (client) -> {
        for (final var listener : listeners) {
            listener.handle(client);
        }
    });

    public static final Event<ScreenCallback.Init> SCREEN_INIT_PRE = EventFactory.createArrayBacked(ScreenCallback.Init.class, (listeners) -> (screen) -> {
        for (final var listener : listeners) {
            listener.handle(screen);
        }
    });

    public static final Event<ScreenCallback.Init> SCREEN_INIT_POST = EventFactory.createArrayBacked(ScreenCallback.Init.class, (listeners) -> (screen) -> {
        for (final var listener : listeners) {
            listener.handle(screen);
        }
    });

    public static final Event<RenderCallback.Gui.BossInfo> RENDER_GUI_BOSS_INFO_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.BossInfo.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.BossInfo> RENDER_GUI_BOSS_INFO_POST = EventFactory.createArrayBacked(RenderCallback.Gui.BossInfo.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.Chat> RENDER_GUI_CHAT_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Chat.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.Chat> RENDER_GUI_CHAT_POST = EventFactory.createArrayBacked(RenderCallback.Gui.Chat.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.PlayerList> RENDER_GUI_PLAYER_LIST_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.PlayerList.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.PlayerList> RENDER_GUI_PLAYER_LIST_POST = EventFactory.createArrayBacked(RenderCallback.Gui.PlayerList.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.Health> RENDER_GUI_HEALTH_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.Health.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui.Health> RENDER_GUI_HEALTH_POST = EventFactory.createArrayBacked(RenderCallback.Gui.Health.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui> RENDER_GUI_PRE = EventFactory.createArrayBacked(RenderCallback.Gui.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<RenderCallback.Gui> RENDER_GUI_POST = EventFactory.createArrayBacked(RenderCallback.Gui.class, (listeners) -> (guiGraphics, window) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(guiGraphics, window));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });
}
