package net.blay09.mods.balm.neoforge.client.platform.event.internal;

import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.client.platform.event.callback.ClientInputCallback;
import net.blay09.mods.balm.client.platform.event.callback.ClientItemCallback;
import net.blay09.mods.balm.client.platform.event.callback.RenderCallback;
import net.blay09.mods.balm.client.platform.event.callback.ScreenCallback;
import net.blay09.mods.balm.client.platform.event.callback.ClientTickCallback;
import net.blay09.mods.balm.client.platform.event.BalmSupplementalClientEvents;
import net.blay09.mods.balm.neoforge.platform.event.internal.NeoForgeBalmEventMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
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

        ScreenCallback.Init.BEFORE.configureMapping(NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE::register);
        ScreenCallback.Init.AFTER.configureMapping(NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_POST::register);
        bindSimple(ScreenCallback.Open.EVENT, ScreenEvent.Opening.class, (event, it) -> {
            final var newScreen = it.handle(event.getScreen());
            if (newScreen != null) {
                event.setNewScreen(newScreen);
            }
        });

        bindSimple(ScreenCallback.Render.BEFORE, ScreenEvent.Render.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.AFTER_BACKGROUND, ScreenEvent.Render.Background.class, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.AFTER, ScreenEvent.Render.Post.class, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindCancelable(ScreenCallback.KeyPress.BEFORE, ScreenEvent.KeyPressed.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyPress.AFTER, ScreenEvent.KeyPressed.Post.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyRelease.BEFORE, ScreenEvent.KeyReleased.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyRelease.AFTER, ScreenEvent.KeyReleased.Post.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.MousePress.BEFORE, ScreenEvent.MouseButtonPressed.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseButtonEvent(), false));
        bindSimple(ScreenCallback.MousePress.AFTER, ScreenEvent.MouseButtonPressed.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseButtonEvent(), false));
        bindCancelable(ScreenCallback.MouseRelease.BEFORE, ScreenEvent.MouseButtonReleased.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindSimple(ScreenCallback.MouseRelease.AFTER, ScreenEvent.MouseButtonReleased.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindCancelable(ScreenCallback.MouseDrag.BEFORE, ScreenEvent.MouseDragged.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseDrag.AFTER, ScreenEvent.MouseDragged.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseScroll.BEFORE, ScreenEvent.MouseScrolled.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY(), false));
        bindSimple(ScreenCallback.MouseScroll.AFTER, ScreenEvent.MouseScrolled.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY(), false));

        bindSimple(ClientInputCallback.Keyboard.EVENT, InputEvent.Key.class, (event, it) -> it.handle(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));

        bindCancelable(ClientItemCallback.Use.EVENT, InputEvent.InteractionKeyMappingTriggered.class, (event, it) -> {
            if (event.isUseItem() && Minecraft.getInstance().player != null) {
                final var result = it.handle(Minecraft.getInstance().player, event.getHand());
                if (result != InteractionResult.PASS) {
                    event.setSwingHand(false);
                    return true;
                }
            }
            return false;
        });

        bindSimple(RenderCallback.UpdateFov.EVENT, ComputeFovModifierEvent.class, (event, it) -> {
            final var newFov = it.handle(event.getPlayer(), event.getFovModifier());
            event.setNewFovModifier(newFov);
        });
        bindCancelable(RenderCallback.Hand.EVENT, RenderHandEvent.class, (event, it) -> it.handle(event.getHand(), event.getItemStack(), event.getSwingProgress()).shouldSkipDefault());
        bindSimple(RenderCallback.Gui.BEFORE, RenderGuiEvent.Pre.class, (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindSimple(RenderCallback.Gui.AFTER, RenderGuiEvent.Post.class, (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Health.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Health.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Chat.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.CHAT.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Chat.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.CHAT.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.BossInfo.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.BOSS_OVERLAY.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.BossInfo.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.BOSS_OVERLAY.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.PlayerList.BEFORE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.TAB_LIST.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.PlayerList.AFTER, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.TAB_LIST.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        RenderCallback.Gui.Debug.BEFORE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.AFTER.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.BlockHighlight.EVENT.configureMapping(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
    }

}

