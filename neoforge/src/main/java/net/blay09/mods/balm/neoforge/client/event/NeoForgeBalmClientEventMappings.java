package net.blay09.mods.balm.neoforge.client.event;

import net.blay09.mods.balm.client.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.client.event.callback.ClientInputCallback;
import net.blay09.mods.balm.client.event.callback.ClientItemCallback;
import net.blay09.mods.balm.client.event.callback.RenderCallback;
import net.blay09.mods.balm.client.event.callback.ScreenCallback;
import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.blay09.mods.balm.client.event.BalmSupplementalClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEventMappings;
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
        bindSimple(ClientTickCallback.PRE, ClientTickEvent.Pre.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientTickCallback.POST, ClientTickEvent.Post.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindFiltered(ClientTickCallback.ClientLevelTick.PRE, LevelTickEvent.Pre.class, event -> event.getLevel().isClientSide(), (event, it) -> it.handle((ClientLevel) event.getLevel()));
        bindFiltered(ClientTickCallback.ClientLevelTick.POST, LevelTickEvent.Post.class, event -> event.getLevel().isClientSide(), (event, it) -> it.handle((ClientLevel) event.getLevel()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.PRE, PlayerTickEvent.Pre.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle((AbstractClientPlayer) event.getEntity()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.POST, PlayerTickEvent.Post.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle((AbstractClientPlayer) event.getEntity()));
        bindFiltered(ClientTickCallback.ClientEntityTick.PRE, EntityTickEvent.Pre.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));
        bindFiltered(ClientTickCallback.ClientEntityTick.POST, EntityTickEvent.Post.class, event -> event.getEntity().level().isClientSide(), (event, it) -> it.handle(event.getEntity()));

        ClientLifecycleCallback.STARTED.configureMapping(NeoForgeBalmSupplementalClientEvents.CLIENT_STARTED::register);
        bindSimple(ClientLifecycleCallback.CONNECTED_TO_SERVER, ClientPlayerNetworkEvent.LoggingIn.class, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientLifecycleCallback.DISCONNECTED_FROM_SERVER, ClientPlayerNetworkEvent.LoggingOut.class, (event, it) -> it.handle(Minecraft.getInstance()));

        ScreenCallback.Init.PRE.configureMapping(NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE::register);
        ScreenCallback.Init.POST.configureMapping(NeoForgeBalmSupplementalClientEvents.SCREEN_INIT_POST::register);
        bindSimple(ScreenCallback.Open.EVENT, ScreenEvent.Opening.class, (event, it) -> {
            final var newScreen = it.handle(event.getScreen());
            if (newScreen != null) {
                event.setNewScreen(newScreen);
            }
        });

        bindSimple(ScreenCallback.Render.PRE, ScreenEvent.Render.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.POST, ScreenEvent.Render.Post.class, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindCancelable(ScreenCallback.KeyPress.PRE, ScreenEvent.KeyPressed.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyPress.POST, ScreenEvent.KeyPressed.Post.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyRelease.PRE, ScreenEvent.KeyReleased.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.KeyRelease.POST, ScreenEvent.KeyReleased.Post.class, (event, it) -> it.handle(event.getScreen(), event.getKeyEvent()));
        bindCancelable(ScreenCallback.MousePress.PRE, ScreenEvent.MouseButtonPressed.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseButtonEvent(), false));
        bindSimple(ScreenCallback.MousePress.POST, ScreenEvent.MouseButtonPressed.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseButtonEvent(), false));
        bindCancelable(ScreenCallback.MouseRelease.PRE, ScreenEvent.MouseButtonReleased.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindSimple(ScreenCallback.MouseRelease.POST, ScreenEvent.MouseButtonReleased.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindCancelable(ScreenCallback.MouseDrag.PRE, ScreenEvent.MouseDragged.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseDrag.POST, ScreenEvent.MouseDragged.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseScroll.PRE, ScreenEvent.MouseScrolled.Pre.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY(), false));
        bindSimple(ScreenCallback.MouseScroll.POST, ScreenEvent.MouseScrolled.Post.class, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDeltaX(), event.getScrollDeltaY(), false));

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
        bindSimple(RenderCallback.Gui.PRE, RenderGuiEvent.Pre.class, (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindSimple(RenderCallback.Gui.POST, RenderGuiEvent.Post.class, (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Health.PRE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Health.POST, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.PLAYER_HEALTH.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Chat.PRE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.CHAT.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.Chat.POST, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.CHAT.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.BossInfo.PRE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.BOSS_OVERLAY.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.BossInfo.POST, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.BOSS_OVERLAY.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.PlayerList.PRE, RenderGuiLayerEvent.Pre.class,
                event -> VanillaGuiLayers.TAB_LIST.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        bindFiltered(RenderCallback.Gui.PlayerList.POST, RenderGuiLayerEvent.Post.class,
                event -> VanillaGuiLayers.TAB_LIST.equals(event.getName()),
                (event, it) -> it.handle(event.getGuiGraphics(), Minecraft.getInstance().getWindow()));
        RenderCallback.Gui.Debug.PRE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.POST.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.BlockHighlight.EVENT.configureMapping(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
    }

}

