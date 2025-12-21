package net.blay09.mods.balm.neoforge.client.platform.event.internal;

import net.blay09.mods.balm.client.platform.event.internal.BalmSupplementalClientEvents;
import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.neoforge.platform.event.internal.NeoForgeBalmEventMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class NeoForgeBalmClientEventMappings extends NeoForgeBalmEventMappings {

    public static void bind() {
        bindSimple(ClientTickCallback.BEFORE, ClientTickEvent.Pre.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientTickCallback.AFTER, ClientTickEvent.Post.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindFiltered(ClientTickCallback.ClientLevelTick.BEFORE, LevelTickEvent.Pre.class, event -> event.getLevel().isClientSide(), (event, it) -> it.handle((ClientLevel) event.getLevel()));
        bindFiltered(ClientTickCallback.ClientLevelTick.AFTER, LevelTickEvent.Post.class, event -> event.getLevel().isClientSide(), (event, it) -> it.handle((ClientLevel) event.getLevel()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.BEFORE, PlayerTickEvent.Pre.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle((AbstractClientPlayer) event.getEntity()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.AFTER, PlayerTickEvent.Post.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle((AbstractClientPlayer) event.getEntity()));
        bindFiltered(ClientTickCallback.ClientEntityTick.BEFORE, EntityTickEvent.Pre.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));
        bindFiltered(ClientTickCallback.ClientEntityTick.AFTER, EntityTickEvent.Post.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));

        ClientLifecycleCallback.Started.EVENT.configureMapping(NeoForgeBalmSupplementalClientEvents.CLIENT_STARTED::register);
        bindSimple(ClientLifecycleCallback.ConnectedToServer.EVENT, ClientPlayerNetworkEvent.LoggingIn.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientLifecycleCallback.DisconnectedFromServer.EVENT, ClientPlayerNetworkEvent.LoggingOut.class, (event, it) -> it.handle(Minecraft.getInstance()));

        ScreenCallback.Init.Before.EVENT.configureMapping(NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE::register);
        ScreenCallback.Init.After.EVENT.configureMapping(NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_POST::register);
        bindSimple(ScreenCallback.Opening.EVENT, ScreenEvent.Opening.class, (event, it) -> {
            final var newScreen = it.modifyScreen(event.getScreen());
            if (newScreen != null) {
                event.setNewScreen(newScreen);
            }
        });

        bindSimple(ScreenCallback.Render.BEFORE, ScreenEvent.Render.Pre.class, (event, it) -> it.render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.AFTER_BACKGROUND, ScreenEvent.Render.Background.class, (event, it) -> it.render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.AFTER, ScreenEvent.Render.Post.class, (event, it) -> it.render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindCancelable(ScreenCallback.KeyPress.Before.EVENT, ScreenEvent.KeyPressed.Pre.class, (event, it) -> it.keyPressed(event.getScreen(), event.getKeyEvent()));
        bindSimple(ScreenCallback.KeyPress.After.EVENT, ScreenEvent.KeyPressed.Post.class, (event, it) -> it.afterKeyPressed(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyRelease.Before.EVENT, ScreenEvent.KeyReleased.Pre.class, (event, it) -> it.keyReleased(event.getScreen(), event.getKeyEvent()));
        bindSimple(ScreenCallback.KeyRelease.After.EVENT, ScreenEvent.KeyReleased.Post.class, (event, it) -> it.afterKeyReleased(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.MousePress.Before.EVENT, ScreenEvent.MouseButtonPressed.Pre.class, (event, it) -> it.mousePressed(event.getScreen(), event.getMouseButtonEvent()));
        bindSimple(ScreenCallback.MousePress.After.EVENT, ScreenEvent.MouseButtonPressed.Post.class, (event, it) -> it.afterMousePressed(event.getScreen(), event.getMouseButtonEvent(), event.getClickResult()));
        bindCancelable(ScreenCallback.MouseRelease.Before.EVENT, ScreenEvent.MouseButtonReleased.Pre.class, (event, it) -> it.mouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()));
        bindSimple(ScreenCallback.MouseRelease.After.EVENT, ScreenEvent.MouseButtonReleased.Post.class, (event, it) -> it.afterMouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), event.getReleaseResult()));
        bindCancelable(ScreenCallback.MouseDrag.Before.EVENT, ScreenEvent.MouseDragged.Pre.class, (event, it) -> it.mouseDragged(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY()));
        bindSimple(ScreenCallback.MouseDrag.After.EVENT, ScreenEvent.MouseDragged.Post.class, (event, it) -> it.afterMouseDragged(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseScroll.Before.EVENT, ScreenEvent.MouseScrolled.Pre.class, (event, it) -> it.mouseScrolled(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY()));
        bindSimple(ScreenCallback.MouseScroll.After.EVENT, ScreenEvent.MouseScrolled.Post.class, (event, it) -> it.afterMouseScrolled(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY(), false));

        bindSimple(ClientInputCallback.Keyboard.EVENT, InputEvent.Key.class, (event, it) -> it.handle(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));

        bindCancelable(ClientItemCallback.Use.EVENT, InputEvent.InteractionKeyMappingTriggered.class, (event, it) -> {
            if (event.isUseItem() && Minecraft.getInstance().player != null) {
                final var result = it.beforeUse(Minecraft.getInstance().player, event.getHand());
                final var interactionResult = result.interactionResult().orElse(null);
                if (interactionResult != null) {
                    event.setSwingHand(interactionResult instanceof InteractionResult.Success success
                            && success.swingSource() == InteractionResult.SwingSource.CLIENT);
                    return true;
                }
            }
            return false;
        });

        bindSimple(RenderCallback.UpdateFov.EVENT, ComputeFovModifierEvent.class, (event, it) -> {
            final var newFov = it.computeFov(event.getPlayer(), event.getFovModifier());
            event.setNewFovModifier(newFov * event.getFovScale());
        });
        bindCancelable(RenderCallback.Hand.EVENT, RenderHandEvent.class, (event, it) -> !it.shouldRender(event.getHand(), event.getItemStack(), event.getSwingProgress()));
        bindCancelable(RenderCallback.Gui.BEFORE, RenderGuiEvent.Pre.class, (event, it) -> !it.shouldRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindSimple(RenderCallback.Gui.AFTER, RenderGuiEvent.Post.class, (event, it) -> it.afterRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Health.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName()),
                (event, it) -> it.shouldRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Health.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName()),
                (event, it) -> it.afterRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Chat.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.CHAT.equals(event.getName()),
                (event, it) -> it.shouldRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Chat.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.CHAT.equals(event.getName()),
                (event, it) -> it.afterRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.BossInfo.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.BOSS_OVERLAY.equals(event.getName()),
                (event, it) -> it.shouldRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.BossInfo.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.BOSS_OVERLAY.equals(event.getName()),
                (event, it) -> it.afterRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.PlayerList.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.TAB_LIST.equals(event.getName()),
                (event, it) -> it.shouldRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.PlayerList.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.TAB_LIST.equals(event.getName()),
                (event, it) -> it.afterRender(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        RenderCallback.Gui.Debug.BEFORE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.AFTER.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.BlockHighlight.EVENT.configureMapping(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
    }

}

