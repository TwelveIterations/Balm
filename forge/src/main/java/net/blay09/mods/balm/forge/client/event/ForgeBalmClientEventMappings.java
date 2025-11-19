package net.blay09.mods.balm.forge.client.event;

import net.blay09.mods.balm.client.event.BalmSupplementalClientEvents;
import net.blay09.mods.balm.client.event.callback.*;
import net.blay09.mods.balm.forge.event.ForgeBalmEventMappings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.LogicalSide;

public class ForgeBalmClientEventMappings extends ForgeBalmEventMappings {
    public static void bind() {
        bindSimple(ClientTickCallback.BEFORE, TickEvent.ClientTickEvent.Pre.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientTickCallback.AFTER, TickEvent.ClientTickEvent.Post.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindFiltered(ClientTickCallback.ClientLevelTick.BEFORE, TickEvent.LevelTickEvent.Pre.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((ClientLevel) event.level()));
        bindFiltered(ClientTickCallback.ClientLevelTick.AFTER, TickEvent.LevelTickEvent.Post.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((ClientLevel) event.level()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.BEFORE, TickEvent.PlayerTickEvent.Pre.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((AbstractClientPlayer) event.player()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.AFTER, TickEvent.PlayerTickEvent.Post.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((AbstractClientPlayer) event.player()));
        bindSimple(ClientTickCallback.ClientEntityTick.BEFORE, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
        bindSimple(ClientTickCallback.ClientEntityTick.AFTER, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));

        ClientLifecycleCallback.Started.EVENT.configureMapping(ForgeBalmSupplementalClientEvents.CLIENT_STARTED::register);
        bindSimple(ClientLifecycleCallback.ConnectedToServer.EVENT, ClientPlayerNetworkEvent.LoggingIn.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientLifecycleCallback.DisconnectedFromServer.EVENT, ClientPlayerNetworkEvent.LoggingOut.BUS, (event, it) -> it.handle(Minecraft.getInstance()));

        ScreenCallback.Init.BEFORE.configureMapping(ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE::register);
        ScreenCallback.Init.AFTER.configureMapping(ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST::register);
        ScreenCallback.Open.EVENT.configureMapping((phase, it)
                -> ScreenEvent.Opening.BUS.addListener(mapPriority(phase), (orig) -> {
            final var newScreen = it.handle(orig.getScreen());
            if (newScreen != null) {
                orig.setNewScreen(newScreen);
            }
            return false;
        }));
        bindSimple(ScreenCallback.Render.BEFORE, ScreenEvent.Render.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.AFTER, ScreenEvent.Render.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindCancelable(ScreenCallback.KeyPress.BEFORE, ScreenEvent.KeyPressed.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyPress.AFTER, ScreenEvent.KeyPressed.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyRelease.BEFORE, ScreenEvent.KeyReleased.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyRelease.AFTER, ScreenEvent.KeyReleased.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.MousePress.BEFORE, ScreenEvent.MouseButtonPressed.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo(), false));
        bindSimple(ScreenCallback.MousePress.AFTER, ScreenEvent.MouseButtonPressed.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo(), false));
        bindCancelable(ScreenCallback.MouseRelease.BEFORE, ScreenEvent.MouseButtonReleased.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindSimple(ScreenCallback.MouseRelease.AFTER, ScreenEvent.MouseButtonReleased.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindCancelable(ScreenCallback.MouseDrag.BEFORE, ScreenEvent.MouseDragged.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseDrag.AFTER, ScreenEvent.MouseDragged.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseScroll.BEFORE, ScreenEvent.MouseScrolled.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY(), false));
        bindSimple(ScreenCallback.MouseScroll.AFTER, ScreenEvent.MouseScrolled.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY(), false));

        RenderCallback.Gui.BEFORE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_PRE::register);
        RenderCallback.Gui.AFTER.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_POST::register);
        RenderCallback.Gui.Health.BEFORE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE::register);
        RenderCallback.Gui.Health.AFTER.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST::register);
        RenderCallback.Gui.Chat.BEFORE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE::register);
        RenderCallback.Gui.Chat.AFTER.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST::register);
        RenderCallback.Gui.BossInfo.BEFORE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_PRE::register);
        RenderCallback.Gui.BossInfo.AFTER.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_POST::register);
        RenderCallback.Gui.PlayerList.BEFORE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE::register);
        RenderCallback.Gui.PlayerList.AFTER.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST::register);
        RenderCallback.Gui.Debug.BEFORE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.AFTER.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.BlockHighlight.EVENT.configureMapping(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
        bindSimple(RenderCallback.UpdateFov.EVENT, ComputeFovModifierEvent.BUS, (event, it) -> {
            final var newFov = it.handle(event.getPlayer(), event.getFovModifier());
            event.setNewFovModifier(newFov);
        });
        bindCancelable(RenderCallback.Hand.EVENT, RenderHandEvent.BUS, (event, it) -> it.handle(event.getHand(), event.getItemStack(), event.getSwingProgress()).shouldSkipDefault());

        bindSimple(ClientInputCallback.Keyboard.EVENT, InputEvent.Key.BUS, (event, it) -> it.handle(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));

        bindCancelable(ClientItemCallback.Use.EVENT, InputEvent.InteractionKeyMappingTriggered.BUS, (event, it) -> {
            if (event.isUseItem() && Minecraft.getInstance().player != null) {
                final var result = it.handle(Minecraft.getInstance().player, event.getHand());
                if (result != InteractionResult.PASS) {
                    event.setSwingHand(false);
                    return true;
                }
            }
            return false;
        });
    }
}
