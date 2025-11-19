package net.blay09.mods.balm.fabric.client.internal.event;

import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.fabric.internal.mixin.ClientLevelAccessor;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.EventHandling;
import net.blay09.mods.balm.platform.event.callback.LevelCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.world.InteractionResult;

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

    public static final Event<ClientItemCallback.Use> CLIENT_USE_ITEM = EventFactory.createArrayBacked(ClientItemCallback.Use.class, (listeners) -> (player, hand) -> {
        InteractionResult result = InteractionResult.PASS;
        for (final var listener : listeners) {
            result = listener.handle(player, hand);
            if (result != InteractionResult.PASS) {
                break;
            }
        }
        return result;
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

    public static final Event<ScreenCallback.MouseDrag> SCREEN_MOUSE_DRAG_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseDrag.class, (listeners) -> (screen, mouseX, mouseY, button, horizontalAmount, verticalAmount, consumed) -> {
        for (final var listener : listeners) {
            if (listener.handle(screen, mouseX, mouseY, button, horizontalAmount, verticalAmount, consumed)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MouseDrag> SCREEN_MOUSE_DRAG_POST = EventFactory.createArrayBacked(ScreenCallback.MouseDrag.class, (listeners) -> (screen, mouseX, mouseY, button, horizontalAmount, verticalAmount, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, mouseX, mouseY, button, horizontalAmount, verticalAmount, cancel | consumed);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.Render> SCREEN_RENDER_PRE = EventFactory.createArrayBacked(ScreenCallback.Render.class, (listeners) -> (screen, guiGraphics, mouseX, mouseY, tickDelta) -> {
        for (final var listener : listeners) {
            listener.handle(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    });

    public static final Event<ScreenCallback.Render> SCREEN_RENDER_POST = EventFactory.createArrayBacked(ScreenCallback.Render.class, (listeners) -> (screen, guiGraphics, mouseX, mouseY, tickDelta) -> {
        for (final var listener : listeners) {
            listener.handle(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    });

    public static final Event<ScreenCallback.KeyPress> SCREEN_KEY_PRESS_PRE = EventFactory.createArrayBacked(ScreenCallback.KeyPress.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.KeyPress> SCREEN_KEY_PRESS_POST = EventFactory.createArrayBacked(ScreenCallback.KeyPress.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.KeyRelease> SCREEN_KEY_RELEASE_PRE = EventFactory.createArrayBacked(ScreenCallback.KeyRelease.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.KeyRelease> SCREEN_KEY_RELEASE_POST = EventFactory.createArrayBacked(ScreenCallback.KeyRelease.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.MousePress> SCREEN_MOUSE_PRESS_PRE = EventFactory.createArrayBacked(ScreenCallback.MousePress.class, (listeners) -> (screen, event, consumed) -> {
        for (final var listener : listeners) {
            if (listener.handle(screen, event, consumed)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MousePress> SCREEN_MOUSE_PRESS_POST = EventFactory.createArrayBacked(ScreenCallback.MousePress.class, (listeners) -> (screen, event, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, event, cancel | consumed);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.MouseRelease> SCREEN_MOUSE_RELEASE_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseRelease.class, (listeners) -> (screen, mouseX, mouseY, button, consumed) -> {
        for (final var listener : listeners) {
            if (listener.handle(screen, mouseX, mouseY, button, consumed)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MouseRelease> SCREEN_MOUSE_RELEASE_POST = EventFactory.createArrayBacked(ScreenCallback.MouseRelease.class, (listeners) -> (screen, mouseX, mouseY, button, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, mouseX, mouseY, button, cancel | consumed);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.MouseScroll> SCREEN_MOUSE_SCROLL_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseScroll.class, (listeners) -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed) -> {
        for (final var listener : listeners) {
            if (listener.handle(screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MouseScroll> SCREEN_MOUSE_SCROLL_POST = EventFactory.createArrayBacked(ScreenCallback.MouseScroll.class, (listeners) -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.handle(screen, mouseX, mouseY, horizontalAmount, verticalAmount, cancel | consumed);
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

        ScreenEvents.BEFORE_INIT.register((client, initScreen, scaledWidth, scaledHeight) -> {
            ScreenEvents.beforeRender(initScreen).register((screen, guiGraphics, mouseX, mouseY, tickDelta) -> SCREEN_RENDER_PRE.invoker().handle(screen, guiGraphics, mouseX, mouseY, tickDelta));
            ScreenEvents.afterRender(initScreen).register((screen, guiGraphics, mouseX, mouseY, tickDelta) -> SCREEN_RENDER_POST.invoker().handle(screen, guiGraphics, mouseX, mouseY, tickDelta));

            ScreenKeyboardEvents.allowKeyPress(initScreen).register((screen, keyEvent) -> !SCREEN_KEY_PRESS_PRE.invoker().handle(screen, keyEvent));
            ScreenKeyboardEvents.afterKeyPress(initScreen).register((screen, keyEvent) -> SCREEN_KEY_PRESS_POST.invoker().handle(screen, keyEvent));

            ScreenKeyboardEvents.allowKeyRelease(initScreen).register((screen, keyEvent) -> !SCREEN_KEY_RELEASE_PRE.invoker().handle(screen, keyEvent));
            ScreenKeyboardEvents.afterKeyRelease(initScreen).register((screen, keyEvent) -> SCREEN_KEY_RELEASE_POST.invoker().handle(screen, keyEvent));

            ScreenMouseEvents.allowMouseClick(initScreen).register((screen, mouseEvent) -> !SCREEN_MOUSE_PRESS_PRE.invoker().handle(screen, mouseEvent, false));
            ScreenMouseEvents.afterMouseClick(initScreen).register((screen, mouseEvent, consumed) -> SCREEN_MOUSE_PRESS_POST.invoker().handle(screen, mouseEvent, consumed));

            ScreenMouseEvents.allowMouseRelease(initScreen).register((screen, mouseEvent) -> !SCREEN_MOUSE_RELEASE_PRE.invoker().handle(screen, mouseEvent.x(), mouseEvent.y(), mouseEvent.button(), false));
            ScreenMouseEvents.afterMouseRelease(initScreen).register((screen, mouseEvent, consumed) -> SCREEN_MOUSE_RELEASE_POST.invoker().handle(screen, mouseEvent.x(), mouseEvent.y(), mouseEvent.button(), consumed));

            ScreenMouseEvents.allowMouseScroll(initScreen).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> !SCREEN_MOUSE_SCROLL_PRE.invoker().handle(screen, mouseX, mouseY, horizontalAmount, verticalAmount, false));
            ScreenMouseEvents.afterMouseScroll(initScreen).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed) -> SCREEN_MOUSE_SCROLL_POST.invoker().handle(screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed));

            ScreenMouseEvents.beforeMouseDrag(initScreen).register((screen, mouseButtonEvent, horizontalAmount, verticalAmount) -> SCREEN_MOUSE_DRAG_PRE.invoker().handle(screen, mouseButtonEvent.x(), mouseButtonEvent.y(), mouseButtonEvent.button(), horizontalAmount, verticalAmount, false));
            ScreenMouseEvents.afterMouseDrag(initScreen).register((screen, mouseButtonEvent, horizontalAmount, verticalAmount, consumed) -> SCREEN_MOUSE_DRAG_POST.invoker().handle(screen, mouseButtonEvent.x(), mouseButtonEvent.y(), mouseButtonEvent.button(), horizontalAmount, verticalAmount, consumed));
        });
    }
}
