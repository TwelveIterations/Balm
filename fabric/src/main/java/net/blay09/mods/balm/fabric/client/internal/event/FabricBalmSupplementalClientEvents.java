package net.blay09.mods.balm.fabric.client.internal.event;

import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.fabric.internal.mixin.ClientLevelAccessor;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.callback.InteractionEventResult;
import net.blay09.mods.balm.platform.event.callback.LevelCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricBalmSupplementalClientEvents {
    public static final Event<ClientTickCallback.ClientPlayerTick> CLIENT_PLAYER_TICK_PRE = EventFactory.createArrayBacked(ClientTickCallback.ClientPlayerTick.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ClientTickCallback.ClientPlayerTick> CLIENT_PLAYER_TICK_POST = EventFactory.createArrayBacked(ClientTickCallback.ClientPlayerTick.class, (listeners) -> (player) -> {
        for (final var listener : listeners) {
            listener.handle(player);
        }
    });

    public static final Event<ClientTickCallback.ClientEntityTick> CLIENT_ENTITY_TICK_PRE = EventFactory.createArrayBacked(ClientTickCallback.ClientEntityTick.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
        }
    });

    public static final Event<ClientTickCallback.ClientEntityTick> CLIENT_ENTITY_TICK_POST = EventFactory.createArrayBacked(ClientTickCallback.ClientEntityTick.class, (listeners) -> (entity) -> {
        for (final var listener : listeners) {
            listener.handle(entity);
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

    public static final Event<RenderCallback.UpdateFov> UPDATE_FOV = EventFactory.createArrayBacked(RenderCallback.UpdateFov.class, (listeners) -> (entity, fov) -> {
        float newFov = fov;
        for (final var listener : listeners) {
            newFov = listener.computeFov(entity, newFov);
        }
        return newFov;
    });

    public static final Event<RenderCallback.Hand> RENDER_HAND = EventFactory.createArrayBacked(RenderCallback.Hand.class, (listeners) -> (hand, itemStack, swingProgress) -> {
        for (final var listener : listeners) {
            if (!listener.shouldRender(hand, itemStack, swingProgress)) {
                return false;
            }
        }
        return true;
    });

    public static final Event<ClientItemCallback.Use> CLIENT_USE_ITEM = EventFactory.createArrayBacked(ClientItemCallback.Use.class, (listeners) -> (player, hand) -> {
        for (final var listener : listeners) {
            final var result = listener.beforeUse(player, hand);
            if(result.interactionResult().isPresent()) {
                return result;
            }
        }
        return InteractionEventResult.DEFAULT;
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

    public static final Event<ScreenCallback.Opening> SCREEN_OPEN = EventFactory.createArrayBacked(ScreenCallback.Opening.class, (listeners) -> (screen) -> {
        var newScreen = screen;
        for (final var listener : listeners) {
            newScreen = listener.modifyScreen(newScreen);
        }
        return newScreen;
    });

    public static void initialize() {
        ClientTickEvents.START_LEVEL_TICK.register(level -> {
            if (CLIENT_PLAYER_TICK_PRE.hasHandlers()) {
                for (final var player : level.players()) {
                    CLIENT_PLAYER_TICK_PRE.invoker().handle(player);
                }
            }
        });
        ClientTickEvents.END_LEVEL_TICK.register(level -> {
            if (CLIENT_PLAYER_TICK_POST.hasHandlers()) {
                for (final var player : level.players()) {
                    CLIENT_PLAYER_TICK_POST.invoker().handle(player);
                }
            }
        });

        ClientTickEvents.START_LEVEL_TICK.register(level -> {
            if (CLIENT_ENTITY_TICK_PRE.hasHandlers()) {
                ((ClientLevelAccessor) level).getTickingEntities().forEach(entity -> CLIENT_ENTITY_TICK_PRE.invoker().handle(entity));
            }
        });
        ClientTickEvents.END_LEVEL_TICK.register(level -> {
            if (CLIENT_ENTITY_TICK_POST.hasHandlers()) {
                ((ClientLevelAccessor) level).getTickingEntities().forEach(entity -> CLIENT_ENTITY_TICK_POST.invoker().handle(entity));
            }
        });
    }
}
