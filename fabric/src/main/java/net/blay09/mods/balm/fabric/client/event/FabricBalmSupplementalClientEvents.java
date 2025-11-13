package net.blay09.mods.balm.fabric.client.event;

import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.client.event.callback.RenderCallback;
import net.blay09.mods.balm.client.event.callback.ClientInputCallback;
import net.blay09.mods.balm.client.event.callback.ScreenCallback;
import net.blay09.mods.balm.event.callback.LevelCallback;
import net.blay09.mods.balm.event.EventHandling;
import net.blay09.mods.balm.event.Event;
import net.blay09.mods.balm.event.EventFactory;
import net.blay09.mods.balm.mixin.ClientLevelAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricBalmSupplementalClientEvents {
    public static final Event<ClientTickCallback.Player> CLIENT_PLAYER_TICK_PRE = EventFactory.createArrayBacked(ClientTickCallback.Player.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ClientTickCallback.Player> CLIENT_PLAYER_TICK_POST = EventFactory.createArrayBacked(ClientTickCallback.Player.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ClientTickCallback.Entity> CLIENT_ENTITY_TICK_PRE = EventFactory.createArrayBacked(ClientTickCallback.Entity.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static final Event<ClientTickCallback.Entity> CLIENT_ENTITY_TICK_POST = EventFactory.createArrayBacked(ClientTickCallback.Entity.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
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

    public static final Event<RenderCallback.UpdateFov> UPDATE_FOV = EventFactory.createArrayBacked(RenderCallback.UpdateFov.class, (listeners) -> (entity, fov) -> {
        float newFov = fov;
        for (final var listener : listeners) {
            newFov = listener.handle(entity, newFov);
        }
        return newFov;
    });

    public static final Event<RenderCallback.Hand> RENDER_HAND = EventFactory.createArrayBacked(RenderCallback.Hand.class, (listeners) -> (hand, itemStack, swingProgress) -> {
        var handling = EventHandling.RESUME;
        for (final var listener : listeners) {
            handling = handling.merge(listener.handle(hand, itemStack, swingProgress));
            if (handling.shouldSkipListeners()) {
                break;
            }
        }
        return handling;
    });

    public static final Event<ClientInputCallback.Keyboard> KEYBOARD_INPUT = EventFactory.createArrayBacked(ClientInputCallback.Keyboard.class, (listeners) -> (key, scanCode, action, modifiers) -> {
        for (final var listener : listeners) {
            listener.handle(key, scanCode, action, modifiers);
        }
    });

    public static final Event<LevelCallback> CLIENT_LEVEL_LOAD = EventFactory.createArrayBacked(LevelCallback.class, (listeners) -> (level) -> {
        for (final var listener : listeners) {
            listener.handle(level);
        }
    });

    public static final Event<LevelCallback> CLIENT_LEVEL_UNLOAD = EventFactory.createArrayBacked(LevelCallback.class, (listeners) -> (level) -> {
        for (final var listener : listeners) {
            listener.handle(level);
        }
    });

    public static final Event<ScreenCallback.Open> SCREEN_OPEN = EventFactory.createArrayBacked(ScreenCallback.Open.class, (listeners) -> (screen) -> {
        var newScreen = screen;
        for (final var listener : listeners) {
            newScreen = listener.handle(newScreen);
        }
        return newScreen;
    });

    public static final Event<ScreenCallback.MouseDrag> SCREEN_MOUSE_DRAG_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseDrag.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.MouseDrag> SCREEN_MOUSE_DRAG_POST = EventFactory.createArrayBacked(ScreenCallback.MouseDrag.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event);
        }
        return cancel;
    });

    public static void initialize() {
        ClientTickEvents.START_WORLD_TICK.register(level -> {
            if (CLIENT_PLAYER_TICK_PRE.hasHandlers()) {
                for (final var player : level.players()) {
                    CLIENT_PLAYER_TICK_PRE.invoker().handle(player);
                }
            }
        });
        ClientTickEvents.END_WORLD_TICK.register(level -> {
            if (CLIENT_PLAYER_TICK_POST.hasHandlers()) {
                for (final var player : level.players()) {
                    CLIENT_PLAYER_TICK_POST.invoker().handle(player);
                }
            }
        });

        ClientTickEvents.START_WORLD_TICK.register(level -> {
            if (CLIENT_ENTITY_TICK_PRE.hasHandlers()) {
                ((ClientLevelAccessor) level).getTickingEntities().forEach(entity -> CLIENT_ENTITY_TICK_PRE.invoker().handle(entity));
            }
        });
        ClientTickEvents.END_WORLD_TICK.register(level -> {
            if (CLIENT_ENTITY_TICK_POST.hasHandlers()) {
                ((ClientLevelAccessor) level).getTickingEntities().forEach(entity -> CLIENT_ENTITY_TICK_POST.invoker().handle(entity));
            }
        });
    }
}
