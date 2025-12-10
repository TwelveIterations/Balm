package net.blay09.mods.balm.forge.client.event.internal;

import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.client.platform.event.internal.BalmSupplementalClientEvents;
import net.blay09.mods.balm.forge.platform.event.internal.ForgeBalmEventMappings;
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

        ScreenCallback.Init.Before.EVENT.configureMapping(ForgeBalmSupplementalClientEvents.SCREEN_INIT_PRE::register);
        ScreenCallback.Init.After.EVENT.configureMapping(ForgeBalmSupplementalClientEvents.SCREEN_INIT_POST::register);
        ScreenCallback.Opening.EVENT.configureMapping((phase, it)
                -> ScreenEvent.Opening.BUS.addListener(mapPriority(phase), (orig) -> {
            final var newScreen = it.modifyScreen(orig.getScreen());
            if (newScreen != null) {
                orig.setNewScreen(newScreen);
            }
            return false;
        }));
        bindSimple(ScreenCallback.Render.BEFORE, ScreenEvent.Render.Pre.BUS, (event, it) -> it.render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindSimple(ScreenCallback.Render.AFTER_BACKGROUND, ScreenEvent.Render.BackgroundRendered.BUS, (event, it) -> it.render(event.getScreen(), event.getGuiGraphics(), 0, 0, 0)); // TODO
        bindSimple(ScreenCallback.Render.AFTER, ScreenEvent.Render.Post.BUS, (event, it) -> it.render(event.getScreen(), event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick()));
        bindCancelable(ScreenCallback.KeyPress.Before.EVENT, ScreenEvent.KeyPressed.Pre.BUS, (event, it) -> it.keyPressed(event.getScreen(), event.getInfo()));
        bindSimple(ScreenCallback.KeyPress.After.EVENT, ScreenEvent.KeyPressed.Post.BUS, (event, it) -> it.afterKeyPressed(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.KeyRelease.Before.EVENT, ScreenEvent.KeyReleased.Pre.BUS, (event, it) -> it.keyReleased(event.getScreen(), event.getInfo()));
        bindSimple(ScreenCallback.KeyRelease.After.EVENT, ScreenEvent.KeyReleased.Post.BUS, (event, it) -> it.afterKeyReleased(event.getScreen(), event.getInfo()));
        bindCancelable(ScreenCallback.MousePress.Before.EVENT, ScreenEvent.MouseButtonPressed.Pre.BUS, (event, it) -> it.mousePressed(event.getScreen(), event.getInfo()));
        bindSimple(ScreenCallback.MousePress.After.EVENT, ScreenEvent.MouseButtonPressed.Post.BUS, (event, it) -> it.afterMousePressed(event.getScreen(), event.getInfo(), event.wasHandled()));
        bindCancelable(ScreenCallback.MouseRelease.Before.EVENT, ScreenEvent.MouseButtonReleased.Pre.BUS, (event, it) -> it.mouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton()));
        bindSimple(ScreenCallback.MouseRelease.After.EVENT, ScreenEvent.MouseButtonReleased.Post.BUS, (event, it) -> it.afterMouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton(), event.wasHandled()));
        bindCancelable(ScreenCallback.MouseDrag.Before.EVENT, ScreenEvent.MouseDragged.Pre.BUS, (event, it) -> it.mouseDragged(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY()));
        bindSimple(ScreenCallback.MouseDrag.After.EVENT, ScreenEvent.MouseDragged.Post.BUS, (event, it) -> it.afterMouseDragged(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY(), false));
        bindSimple(ScreenCallback.MouseScroll.Before.EVENT, ScreenEvent.MouseScrolled.Pre.BUS, (event, it) -> it.mouseScrolled(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY()));
        bindSimple(ScreenCallback.MouseScroll.After.EVENT, ScreenEvent.MouseScrolled.Post.BUS, (event, it) -> it.afterMouseScrolled(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY(), false));

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
            final var newFov = it.computeFov(event.getPlayer(), event.getFovModifier());
            event.setNewFovModifier(newFov);
        });
        bindCancelable(RenderCallback.Hand.EVENT, RenderHandEvent.BUS, (event, it) -> !it.shouldRender(event.getHand(), event.getItemStack(), event.getSwingProgress()));

        bindSimple(ClientInputCallback.Keyboard.EVENT, InputEvent.Key.BUS, (event, it) -> it.handle(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));

        bindCancelable(ClientItemCallback.Use.EVENT, InputEvent.InteractionKeyMappingTriggered.BUS, (event, it) -> {
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
    }
}
