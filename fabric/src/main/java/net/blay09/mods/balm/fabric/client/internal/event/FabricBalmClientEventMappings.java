package net.blay09.mods.balm.fabric.client.internal.event;

import net.blay09.mods.balm.client.platform.event.internal.BalmSupplementalClientEvents;
import net.blay09.mods.balm.client.platform.event.callback.*;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmEventMappings;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;

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

        ScreenCallback.Init.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.beforeInit(screen)));
        ScreenCallback.Init.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.AFTER_INIT.register(mapPhase(phase), (client, screen, scaledWidth, scaledHeight) -> it.afterInit(screen)));
        ScreenCallback.Opening.EVENT.configureMapping(FabricBalmSupplementalClientEvents.SCREEN_OPEN::register);

        ScreenCallback.Render.BEFORE.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenEvents.beforeRender(initScreen).register(mapPhase(phase), it::render)));
        ScreenCallback.Render.AFTER_BACKGROUND.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenEvents.afterBackground(initScreen).register(mapPhase(phase), it::render)));
        ScreenCallback.Render.AFTER.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenEvents.afterRender(initScreen).register(mapPhase(phase), it::render)));
        ScreenCallback.KeyPress.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenKeyboardEvents.allowKeyPress(initScreen).register(mapPhase(phase), (screen1, event1) -> !it.keyPressed(screen1, event1))));
        ScreenCallback.KeyPress.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenKeyboardEvents.afterKeyPress(initScreen).register(mapPhase(phase), it::afterKeyPressed)));
        ScreenCallback.KeyRelease.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenKeyboardEvents.allowKeyRelease(initScreen).register(mapPhase(phase), (screen1, event1) -> !it.keyReleased(screen1, event1))));
        ScreenCallback.KeyRelease.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenKeyboardEvents.afterKeyRelease(initScreen).register(mapPhase(phase), it::afterKeyReleased)));
        ScreenCallback.MousePress.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.allowMouseClick(initScreen).register(mapPhase(phase), (screen1, event1) -> !it.mousePressed(screen1, event1))));
        ScreenCallback.MousePress.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.afterMouseClick(initScreen).register(mapPhase(phase), it::afterMousePressed)));
        ScreenCallback.MouseRelease.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.allowMouseRelease(initScreen).register(mapPhase(phase), (screen, event) -> !it.mouseReleased(screen, event.x(), event.y(), event.button()))));
        ScreenCallback.MouseRelease.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.afterMouseRelease(initScreen).register(mapPhase(phase), (screen, event, consumed) -> it.afterMouseReleased(screen, event.x(), event.y(), event.button(), consumed))));
        ScreenCallback.MouseScroll.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.allowMouseScroll(initScreen).register(mapPhase(phase), (screen1, mouseX, mouseY, horizontalAmount1, verticalAmount1) -> !it.mouseScrolled(screen1, mouseX, mouseY, horizontalAmount1, verticalAmount1))));
        ScreenCallback.MouseScroll.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.afterMouseScroll(initScreen).register(mapPhase(phase), it::afterMouseScrolled)));
        ScreenCallback.MouseDrag.Before.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.allowMouseDrag(initScreen).register(mapPhase(phase), (screen, event, horizontalAmount, verticalAmount) -> !it.mouseDragged(screen, event.x(), event.y(), event.button(), horizontalAmount, verticalAmount))));
        ScreenCallback.MouseDrag.After.EVENT.configureMapping((phase, it)
                -> ScreenEvents.BEFORE_INIT.register(mapPhase(phase), (client, initScreen, scaledWidth, scaledHeight)
                -> ScreenMouseEvents.afterMouseDrag(initScreen).register(mapPhase(phase), (screen, event, horizontalAmount, verticalAmount, consumed) -> it.afterMouseDragged(screen, event.x(), event.y(), event.button(), horizontalAmount, verticalAmount, consumed))));
    }

}
