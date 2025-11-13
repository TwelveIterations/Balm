package net.blay09.mods.balm.fabric.client.event;

import net.blay09.mods.balm.client.event.BalmSupplementalClientEvents;
import net.blay09.mods.balm.client.event.callback.*;
import net.blay09.mods.balm.fabric.event.FabricBalmEventMappings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class FabricBalmClientEventMappings extends FabricBalmEventMappings {

    public static void bind() {
        ClientTickCallback.PRE.setup((phase, it)
                -> ClientTickEvents.START_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.POST.setup((phase, it)
                -> ClientTickEvents.END_CLIENT_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.Level.PRE.setup((phase, it)
                -> ClientTickEvents.START_WORLD_TICK.register(mapPhase(phase), it::handle));
        ClientTickCallback.Level.POST.setup((phase, it)
                -> ClientTickEvents.END_WORLD_TICK.register(mapPhase(phase), it::handle));

        ClientTickCallback.Player.PRE.setup(FabricBalmSupplementalClientEvents.CLIENT_PLAYER_TICK_PRE::register);
        ClientTickCallback.Player.POST.setup(FabricBalmSupplementalClientEvents.CLIENT_PLAYER_TICK_POST::register);
        ClientTickCallback.Entity.PRE.setup(FabricBalmSupplementalClientEvents.CLIENT_ENTITY_TICK_PRE::register);
        ClientTickCallback.Entity.POST.setup(FabricBalmSupplementalClientEvents.CLIENT_ENTITY_TICK_POST::register);

        ClientLifecycleCallback.STARTED.setup((phase, it)
                -> ClientLifecycleEvents.CLIENT_STARTED.register(mapPhase(phase), it::handle));
        ClientLifecycleCallback.CONNECTED_TO_SERVER.setup((phase, it)
                -> ClientPlayConnectionEvents.JOIN.register(mapPhase(phase), (clientPacketListener, packetSender, client) -> it.handle(client)));
        ClientLifecycleCallback.DISCONNECTED_FROM_SERVER.setup((phase, it)
                -> ClientPlayConnectionEvents.DISCONNECT.register(mapPhase(phase), ((clientPacketListener, client) -> it.handle(client))));

        RenderCallback.BlockHighlight.EVENT.setup(BalmSupplementalClientEvents.RENDER_BLOCK_HIGHLIGHT::register);
        RenderCallback.Gui.PRE.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_PRE::register);
        RenderCallback.Gui.POST.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_POST::register);
        RenderCallback.Gui.Health.PRE.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_PRE::register);
        RenderCallback.Gui.Health.POST.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_HEALTH_POST::register);
        RenderCallback.Gui.Chat.PRE.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_PRE::register);
        RenderCallback.Gui.Chat.POST.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_CHAT_POST::register);
        RenderCallback.Gui.BossInfo.PRE.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_PRE::register);
        RenderCallback.Gui.BossInfo.POST.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_BOSS_INFO_POST::register);
        RenderCallback.Gui.PlayerList.PRE.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_PRE::register);
        RenderCallback.Gui.PlayerList.POST.setup(FabricBalmSupplementalClientEvents.RENDER_GUI_PLAYER_LIST_POST::register);
        RenderCallback.Gui.Debug.PRE.setup(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_PRE::register);
        RenderCallback.Gui.Debug.POST.setup(BalmSupplementalClientEvents.RENDER_GUI_DEBUG_POST::register);
        RenderCallback.UpdateFov.EVENT.setup(FabricBalmSupplementalClientEvents.UPDATE_FOV::register);
        RenderCallback.Hand.EVENT.setup(FabricBalmSupplementalClientEvents.RENDER_HAND::register);

        ClientInputCallback.Keyboard.EVENT.setup(FabricBalmSupplementalClientEvents.KEYBOARD_INPUT::register);
        ClientItemCallback.Use.EVENT.setup(FabricBalmSupplementalClientEvents.CLIENT_USE_ITEM::register);

        ScreenCallback.Init.PRE.setup((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.handle(screen)));
        ScreenCallback.Init.POST.setup((phase, it)
                -> ScreenEvents.AFTER_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.handle(screen)));
        ScreenCallback.Open.EVENT.setup(FabricBalmSupplementalClientEvents.SCREEN_OPEN::register);
        ScreenCallback.Render.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_RENDER_PRE::register);
        ScreenCallback.Render.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_RENDER_POST::register);
        ScreenCallback.KeyPress.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_KEY_PRESS_PRE::register);
        ScreenCallback.KeyPress.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_KEY_PRESS_POST::register);
        ScreenCallback.KeyRelease.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_KEY_RELEASE_PRE::register);
        ScreenCallback.KeyRelease.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_KEY_RELEASE_POST::register);
        ScreenCallback.MousePress.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_PRESS_PRE::register);
        ScreenCallback.MousePress.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_PRESS_POST::register);
        ScreenCallback.MouseRelease.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_RELEASE_PRE::register);
        ScreenCallback.MouseRelease.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_RELEASE_POST::register);
        ScreenCallback.MouseScroll.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_SCROLL_PRE::register);
        ScreenCallback.MouseScroll.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_SCROLL_POST::register);
        ScreenCallback.MouseDrag.PRE.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_DRAG_PRE::register);
        ScreenCallback.MouseDrag.POST.setup(FabricBalmSupplementalClientEvents.SCREEN_MOUSE_DRAG_POST::register);
    }

}
