package net.blay09.mods.balm.fabric.client.event;

import net.blay09.mods.balm.client.event.BalmSupplementalClientEvents;
import net.blay09.mods.balm.client.event.callback.*;
import net.blay09.mods.balm.fabric.event.FabricBalmEventMappings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class FabricBalmClientEventMappings extends FabricBalmEventMappings {

    public static void bind() {
        ClientTickCallback.PRE.configureMapping((phase, it)
                -> ClientTickEvents.START_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.POST.configureMapping((phase, it)
                -> ClientTickEvents.END_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.Level.PRE.configureMapping((phase, it)
                -> ClientTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.Level.POST.configureMapping((phase, it)
                -> ClientTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));

        ClientTickCallback.Player.PRE.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_PLAYER_TICK_PRE::register);
        ClientTickCallback.Player.POST.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_PLAYER_TICK_POST::register);
        ClientTickCallback.Entity.PRE.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_ENTITY_TICK_PRE::register);
        ClientTickCallback.Entity.POST.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_ENTITY_TICK_POST::register);

        ClientLifecycleCallback.STARTED.configureMapping((phase, it)
                -> ClientLifecycleEvents.CLIENT_STARTED.register(mapPhase(phase), it::handle));
        ClientLifecycleCallback.CONNECTED_TO_SERVER.configureMapping((phase, it)
                -> ClientPlayConnectionEvents.JOIN.register(mapPhase(phase), (clientPacketListener, packetSender, client) -> it.handle(client)));
        ClientLifecycleCallback.DISCONNECTED_FROM_SERVER.configureMapping((phase, it)
                -> ClientPlayConnectionEvents.DISCONNECT.register(mapPhase(phase), ((clientPacketListener, client) -> it.handle(client))));

        RenderCallback.BlockHighlight.EVENT.configureMapping(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
        RenderCallback.Gui.PRE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_PRE::register);
        RenderCallback.Gui.POST.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_POST::register);
        RenderCallback.Gui.Health.PRE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE::register);
        RenderCallback.Gui.Health.POST.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST::register);
        RenderCallback.Gui.Chat.PRE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE::register);
        RenderCallback.Gui.Chat.POST.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST::register);
        RenderCallback.Gui.BossInfo.PRE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_PRE::register);
        RenderCallback.Gui.BossInfo.POST.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_POST::register);
        RenderCallback.Gui.PlayerList.PRE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE::register);
        RenderCallback.Gui.PlayerList.POST.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST::register);
        RenderCallback.Gui.Debug.PRE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.POST.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.UpdateFov.EVENT.configureMapping(FabricBalmSupplementalClientEvents.UPDATE_FOV::register);
        RenderCallback.Hand.EVENT.configureMapping(FabricBalmSupplementalClientEvents.RENDER_HAND::register);

        ClientInputCallback.Keyboard.EVENT.configureMapping(FabricBalmSupplementalClientEvents.KEYBOARD_INPUT::register);
        ClientItemCallback.Use.EVENT.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_USE_ITEM::register);

        ScreenCallback.Init.PRE.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.handle(screen)));
        ScreenCallback.Init.POST.configureMapping((phase, it)
                -> ScreenEvents.AFTER_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.handle(screen)));
        ScreenCallback.Open.EVENT.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_OPEN::register);
        ScreenCallback.Render.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_RENDER_PRE::register);
        ScreenCallback.Render.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_RENDER_POST::register);
        ScreenCallback.KeyPress.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_PRESS_PRE::register);
        ScreenCallback.KeyPress.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_PRESS_POST::register);
        ScreenCallback.KeyRelease.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_RELEASE_PRE::register);
        ScreenCallback.KeyRelease.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_RELEASE_POST::register);
        ScreenCallback.MousePress.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_PRESS_PRE::register);
        ScreenCallback.MousePress.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_PRESS_POST::register);
        ScreenCallback.MouseRelease.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_RELEASE_PRE::register);
        ScreenCallback.MouseRelease.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_RELEASE_POST::register);
        ScreenCallback.MouseScroll.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_SCROLL_PRE::register);
        ScreenCallback.MouseScroll.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_SCROLL_POST::register);
        ScreenCallback.MouseDrag.PRE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_DRAG_PRE::register);
        ScreenCallback.MouseDrag.POST.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_DRAG_POST::register);
    }

}
