package net.blay09.mods.balm.fabric.client.internal.event;

import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.fabric.internal.mixin.ClientLevelAccessor;
import net.blay09.mods.balm.platform.event.Event;
import net.blay09.mods.balm.platform.event.EventFactory;
import net.blay09.mods.balm.platform.event.callback.InteractionEventResult;
import net.blay09.mods.balm.platform.event.callback.LevelCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;

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

    public static final Event<ScreenCallback.MouseDrag.Before> SCREEN_MOUSE_DRAG_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseDrag.Before.class, (listeners) -> (screen, mouseX, mouseY, button, horizontalAmount, verticalAmount) -> {
        for (final var listener : listeners) {
            if (listener.mouseDragged(screen, mouseX, mouseY, button, horizontalAmount, verticalAmount)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MouseDrag.After> SCREEN_MOUSE_DRAG_POST = EventFactory.createArrayBacked(ScreenCallback.MouseDrag.After.class, (listeners) -> (screen, mouseX, mouseY, button, horizontalAmount, verticalAmount, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.afterMouseDragged(screen, mouseX, mouseY, button, horizontalAmount, verticalAmount, cancel | consumed);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.Render> SCREEN_RENDER_PRE = EventFactory.createArrayBacked(ScreenCallback.Render.class, (listeners) -> (screen, guiGraphics, mouseX, mouseY, tickDelta) -> {
        for (final var listener : listeners) {
            listener.render(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    });

    public static final Event<ScreenCallback.Render> SCREEN_RENDER_POST = EventFactory.createArrayBacked(ScreenCallback.Render.class, (listeners) -> (screen, guiGraphics, mouseX, mouseY, tickDelta) -> {
        for (final var listener : listeners) {
            listener.render(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    });

    public static final Event<ScreenCallback.Render> SCREEN_RENDER_BACKGROUND_POST = EventFactory.createArrayBacked(ScreenCallback.Render.class, (listeners) -> (screen, guiGraphics, mouseX, mouseY, tickDelta) -> {
        for (final var listener : listeners) {
            listener.render(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    });

    public static final Event<ScreenCallback.KeyPress.Before> SCREEN_KEY_PRESS_PRE = EventFactory.createArrayBacked(ScreenCallback.KeyPress.Before.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.keyPressed(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.KeyPress.After> SCREEN_KEY_PRESS_POST = EventFactory.createArrayBacked(ScreenCallback.KeyPress.After.class, (listeners) -> (screen, event) -> {
        for (final var listener : listeners) {
            listener.afterKeyPressed(screen, event);
        }
    });

    public static final Event<ScreenCallback.KeyRelease.Before> SCREEN_KEY_RELEASE_PRE = EventFactory.createArrayBacked(ScreenCallback.KeyRelease.Before.class, (listeners) -> (screen, event) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.keyReleased(screen, event);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.KeyRelease.After> SCREEN_KEY_RELEASE_POST = EventFactory.createArrayBacked(ScreenCallback.KeyRelease.After.class, (listeners) -> (screen, event) -> {
        for (final var listener : listeners) {
            listener.afterKeyReleased(screen, event);
        }
    });

    public static final Event<ScreenCallback.MousePress.Before> SCREEN_MOUSE_PRESS_PRE = EventFactory.createArrayBacked(ScreenCallback.MousePress.Before.class, (listeners) -> (screen, event) -> {
        for (final var listener : listeners) {
            if (listener.mousePressed(screen, event)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MousePress.After> SCREEN_MOUSE_PRESS_POST = EventFactory.createArrayBacked(ScreenCallback.MousePress.After.class, (listeners) -> (screen, event, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.afterMousePressed(screen, event, cancel | consumed);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.MouseRelease.Before> SCREEN_MOUSE_RELEASE_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseRelease.Before.class, (listeners) -> (screen, mouseX, mouseY, button) -> {
        for (final var listener : listeners) {
            if (listener.mouseReleased(screen, mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MouseRelease.After> SCREEN_MOUSE_RELEASE_POST = EventFactory.createArrayBacked(ScreenCallback.MouseRelease.After.class, (listeners) -> (screen, mouseX, mouseY, button, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.afterMouseReleased(screen, mouseX, mouseY, button, cancel | consumed);
        }
        return cancel;
    });

    public static final Event<ScreenCallback.MouseScroll.Before> SCREEN_MOUSE_SCROLL_PRE = EventFactory.createArrayBacked(ScreenCallback.MouseScroll.Before.class, (listeners) -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
        for (final var listener : listeners) {
            if (listener.mouseScrolled(screen, mouseX, mouseY, horizontalAmount, verticalAmount)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<ScreenCallback.MouseScroll.After> SCREEN_MOUSE_SCROLL_POST = EventFactory.createArrayBacked(ScreenCallback.MouseScroll.After.class, (listeners) -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed) -> {
        boolean cancel = false;
        for (final var listener : listeners) {
            cancel |= listener.afterMouseScrolled(screen, mouseX, mouseY, horizontalAmount, verticalAmount, cancel | consumed);
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
            ScreenEvents.beforeRender(initScreen).register((screen, guiGraphics, mouseX, mouseY, tickDelta) -> SCREEN_RENDER_PRE.invoker().render(screen, guiGraphics, mouseX, mouseY, tickDelta));
            ScreenEvents.afterRender(initScreen).register((screen, guiGraphics, mouseX, mouseY, tickDelta) -> SCREEN_RENDER_POST.invoker().render(screen, guiGraphics, mouseX, mouseY, tickDelta));
            ScreenEvents.afterBackground(initScreen).register((screen, guiGraphics, mouseX, mouseY, tickDelta) -> SCREEN_RENDER_BACKGROUND_POST.invoker().render(screen, guiGraphics, mouseX, mouseY, tickDelta));

            ScreenKeyboardEvents.allowKeyPress(initScreen).register((screen, keyEvent) -> !SCREEN_KEY_PRESS_PRE.invoker().keyPressed(screen, keyEvent));
            ScreenKeyboardEvents.afterKeyPress(initScreen).register((screen, keyEvent) -> SCREEN_KEY_PRESS_POST.invoker().afterKeyPressed(screen, keyEvent));

            ScreenKeyboardEvents.allowKeyRelease(initScreen).register((screen, keyEvent) -> !SCREEN_KEY_RELEASE_PRE.invoker().keyReleased(screen, keyEvent));
            ScreenKeyboardEvents.afterKeyRelease(initScreen).register((screen, keyEvent) -> SCREEN_KEY_RELEASE_POST.invoker().afterKeyReleased(screen, keyEvent));

            ScreenMouseEvents.allowMouseClick(initScreen).register((screen, mouseEvent) -> !SCREEN_MOUSE_PRESS_PRE.invoker().mousePressed(screen, mouseEvent));
            ScreenMouseEvents.afterMouseClick(initScreen).register((screen, mouseEvent, consumed) -> SCREEN_MOUSE_PRESS_POST.invoker().afterMousePressed(screen, mouseEvent, consumed));

            ScreenMouseEvents.allowMouseRelease(initScreen).register((screen, mouseEvent) -> !SCREEN_MOUSE_RELEASE_PRE.invoker().mouseReleased(screen, mouseEvent.x(), mouseEvent.y(), mouseEvent.button()));
            ScreenMouseEvents.afterMouseRelease(initScreen).register((screen, mouseEvent, consumed) -> SCREEN_MOUSE_RELEASE_POST.invoker().afterMouseReleased(screen, mouseEvent.x(), mouseEvent.y(), mouseEvent.button(), consumed));

            ScreenMouseEvents.allowMouseScroll(initScreen).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> !SCREEN_MOUSE_SCROLL_PRE.invoker().mouseScrolled(screen, mouseX, mouseY, horizontalAmount, verticalAmount));
            ScreenMouseEvents.afterMouseScroll(initScreen).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed) -> SCREEN_MOUSE_SCROLL_POST.invoker().afterMouseScrolled(screen, mouseX, mouseY, horizontalAmount, verticalAmount, consumed));

            ScreenMouseEvents.beforeMouseDrag(initScreen).register((screen, mouseButtonEvent, horizontalAmount, verticalAmount) -> SCREEN_MOUSE_DRAG_PRE.invoker().mouseDragged(screen, mouseButtonEvent.x(), mouseButtonEvent.y(), mouseButtonEvent.button(), horizontalAmount, verticalAmount));
            ScreenMouseEvents.afterMouseDrag(initScreen).register((screen, mouseButtonEvent, horizontalAmount, verticalAmount, consumed) -> SCREEN_MOUSE_DRAG_POST.invoker().afterMouseDragged(screen, mouseButtonEvent.x(), mouseButtonEvent.y(), mouseButtonEvent.button(), horizontalAmount, verticalAmount, consumed));
        });
    }
}
