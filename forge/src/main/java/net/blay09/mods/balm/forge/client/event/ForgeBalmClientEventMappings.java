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
        bindSimple(ClientTickCallback.PRE, TickEvent.ClientTickEvent.Pre.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientTickCallback.POST, TickEvent.ClientTickEvent.Post.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindFiltered(ClientTickCallback.ClientLevelTick.PRE, TickEvent.LevelTickEvent.Pre.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((ClientLevel) event.level()));
        bindFiltered(ClientTickCallback.ClientLevelTick.POST, TickEvent.LevelTickEvent.Post.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((ClientLevel) event.level()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.PRE, TickEvent.PlayerTickEvent.Pre.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((AbstractClientPlayer) event.player()));
        bindFiltered(ClientTickCallback.ClientPlayerTick.POST, TickEvent.PlayerTickEvent.Post.BUS, event -> event.side() == LogicalSide.CLIENT, (event, it) -> it.handle((AbstractClientPlayer) event.player()));
        bindSimple(ClientTickCallback.ClientEntityTick.PRE, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));
        bindSimple(ClientTickCallback.ClientEntityTick.POST, LivingEvent.LivingTickEvent.BUS, (event, it) -> it.handle(event.getEntity()));

        ClientLifecycleCallback.STARTED.configureMapping(ForgeBalmSupplementalClientEvents.CLIENT_STARTED::register);
        bindSimple(ClientLifecycleCallback.CONNECTED_TO_SERVER, ClientPlayerNetworkEvent.LoggingIn.BUS, (event, it) -> it.handle(Minecraft.getInstance()));
        bindSimple(ClientLifecycleCallback.DISCONNECTED_FROM_SERVER, ClientPlayerNetworkEvent.LoggingOut.BUS, (event, it) -> it.handle(Minecraft.getInstance()));

        ScreenCallback.Init.PRE.configureMapping(ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE::register);
        ScreenCallback.Init.POST.configureMapping(ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST::register);
        ScreenCallback.Open.EVENT.configureMapping((phase, it)
                -> ScreenEvent.Opening.BUS.addListener(mapPriority(phase), (orig) -> {
            final var newScreen = it.handle(orig.getScreen());
            if (newScreen != null) {
                orig.setNewScreen(newScreen);
            }
            return false;
        }));
        bindSimple(ScreenCallback.Render.PRE, ScreenEvent.Render.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.POST, ScreenEvent.Render.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindCancelable(ScreenCallback.KeyPress.PRE, ScreenEvent.KeyPressed.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyPress.POST, ScreenEvent.KeyPressed.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyRelease.PRE, ScreenEvent.KeyReleased.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyRelease.POST, ScreenEvent.KeyReleased.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.MousePress.PRE, ScreenEvent.MouseButtonPressed.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo(), false));
        bindSimple(ScreenCallback.MousePress.POST, ScreenEvent.MouseButtonPressed.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getInfo(), false));
        bindCancelable(ScreenCallback.MouseRelease.PRE, ScreenEvent.MouseButtonReleased.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindSimple(ScreenCallback.MouseRelease.POST, ScreenEvent.MouseButtonReleased.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), false));
        bindCancelable(ScreenCallback.MouseDrag.PRE, ScreenEvent.MouseDragged.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseDrag.POST, ScreenEvent.MouseDragged.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseScroll.PRE, ScreenEvent.MouseScrolled.Pre.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY(), false));
        bindSimple(ScreenCallback.MouseScroll.POST, ScreenEvent.MouseScrolled.Post.BUS, (event, it) -> it.handle(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY(), false));

        RenderCallback.Gui.PRE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_PRE::register);
        RenderCallback.Gui.POST.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_POST::register);
        RenderCallback.Gui.Health.PRE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE::register);
        RenderCallback.Gui.Health.POST.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST::register);
        RenderCallback.Gui.Chat.PRE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE::register);
        RenderCallback.Gui.Chat.POST.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST::register);
        RenderCallback.Gui.BossInfo.PRE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_PRE::register);
        RenderCallback.Gui.BossInfo.POST.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_POST::register);
        RenderCallback.Gui.PlayerList.PRE.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE::register);
        RenderCallback.Gui.PlayerList.POST.configureMapping(ForgeBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST::register);
        RenderCallback.Gui.Debug.PRE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.POST.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
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
