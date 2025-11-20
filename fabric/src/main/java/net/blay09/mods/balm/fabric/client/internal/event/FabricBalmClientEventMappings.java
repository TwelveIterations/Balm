package net.blay09.mods.balm.fabric.client.internal.event;

import net.blay09.mods.balm.client.platform.event.BalmSupplementalClientEvents;
import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmEventMappings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class FabricBalmClientEventMappings extends FabricBalmEventMappings {

    public static void bind() {
        ClientTickCallback.BEFORE.configureMapping((phase, it)
                -> ClientTickEvents.START_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.AFTER.configureMapping((phase, it)
                -> ClientTickEvents.END_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.ClientLevelTick.BEFORE.configureMapping((phase, it)
                -> ClientTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.ClientLevelTick.AFTER.configureMapping((phase, it)
                -> ClientTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));

        ClientTickCallback.ClientPlayerTick.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_PLAYER_TICK_PRE::register);
        ClientTickCallback.ClientPlayerTick.AFTER.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_PLAYER_TICK_POST::register);
        ClientTickCallback.ClientEntityTick.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_ENTITY_TICK_PRE::register);
        ClientTickCallback.ClientEntityTick.AFTER.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_ENTITY_TICK_POST::register);

        ClientLifecycleCallback.Started.EVENT.configureMapping((phase, it)
                -> ClientLifecycleEvents.CLIENT_STARTED.register(mapPhase(phase), it::handle));
        ClientLifecycleCallback.ConnectedToServer.EVENT.configureMapping((phase, it)
                -> ClientPlayConnectionEvents.JOIN.register(mapPhase(phase), (clientPacketListener, packetSender, client) -> it.handle(client)));
        ClientLifecycleCallback.DisconnectedFromServer.EVENT.configureMapping((phase, it)
                -> ClientPlayConnectionEvents.DISCONNECT.register(mapPhase(phase), ((clientPacketListener, client) -> it.handle(client))));

        RenderCallback.BlockHighlight.EVENT.configureMapping(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
        RenderCallback.Gui.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_PRE::register);
        RenderCallback.Gui.AFTER.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_POST::register);
        RenderCallback.Gui.Health.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE::register);
        RenderCallback.Gui.Health.AFTER.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST::register);
        RenderCallback.Gui.Chat.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE::register);
        RenderCallback.Gui.Chat.AFTER.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST::register);
        RenderCallback.Gui.BossInfo.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_PRE::register);
        RenderCallback.Gui.BossInfo.AFTER.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_POST::register);
        RenderCallback.Gui.PlayerList.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE::register);
        RenderCallback.Gui.PlayerList.AFTER.configureMapping(FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST::register);
        RenderCallback.Gui.Debug.BEFORE.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.AFTER.configureMapping(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.UpdateFov.EVENT.configureMapping(FabricBalmSupplementalClientEvents.UPDATE_FOV::register);
        RenderCallback.Hand.EVENT.configureMapping(FabricBalmSupplementalClientEvents.RENDER_HAND::register);

        ClientInputCallback.Keyboard.EVENT.configureMapping(FabricBalmSupplementalClientEvents.KEYBOARD_INPUT::register);
        ClientItemCallback.Use.EVENT.configureMapping(FabricBalmSupplementalClientEvents.CLIENT_USE_ITEM::register);

        ScreenCallback.Init.BEFORE.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.handle(screen)));
        ScreenCallback.Init.AFTER.configureMapping((phase, it)
                -> ScreenEvents.AFTER_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.handle(screen)));
        ScreenCallback.Open.EVENT.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_OPEN::register);
        ScreenCallback.Render.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_RENDER_PRE::register);
        ScreenCallback.Render.AFTER_BACKGROUND.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_RENDER_BACKGROUND_POST::register);
        ScreenCallback.Render.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_RENDER_POST::register);
        ScreenCallback.KeyPress.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_PRESS_PRE::register);
        ScreenCallback.KeyPress.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_PRESS_POST::register);
        ScreenCallback.KeyRelease.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_RELEASE_PRE::register);
        ScreenCallback.KeyRelease.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_KEY_RELEASE_POST::register);
        ScreenCallback.MousePress.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_PRESS_PRE::register);
        ScreenCallback.MousePress.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_PRESS_POST::register);
        ScreenCallback.MouseRelease.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_RELEASE_PRE::register);
        ScreenCallback.MouseRelease.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_RELEASE_POST::register);
        ScreenCallback.MouseScroll.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_SCROLL_PRE::register);
        ScreenCallback.MouseScroll.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_SCROLL_POST::register);
        ScreenCallback.MouseDrag.BEFORE.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_DRAG_PRE::register);
        ScreenCallback.MouseDrag.AFTER.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_DRAG_POST::register);
    }

}
