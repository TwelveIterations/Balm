package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.blay09.mods.balm.api.event.client.RenderHandEvent;
import net.blay09.mods.balm.api.event.client.*;
import net.blay09.mods.balm.api.event.client.screen.ContainerScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenKeyEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;

public class ForgeBalmClientEvents {

    public static void registerEvents(ForgeBalmEvents events) {
        events.registerTickEvent(TickType.Client, TickPhase.Start, (ClientTickHandler handler) -> TickEvent.ClientTickEvent.Pre.BUS.addListener((orig) -> handler.handle(Minecraft.getInstance())));
        events.registerTickEvent(TickType.Client, TickPhase.End, (ClientTickHandler handler) -> TickEvent.ClientTickEvent.Post.BUS.addListener((orig) -> handler.handle(Minecraft.getInstance())));
        events.registerTickEvent(TickType.ClientLevel, TickPhase.Start, (ClientLevelTickHandler handler) -> TickEvent.ClientTickEvent.Pre.BUS.addListener((orig) -> handler.handle(Minecraft.getInstance().level)));
        events.registerTickEvent(TickType.ClientLevel, TickPhase.End, (ClientLevelTickHandler handler) -> TickEvent.ClientTickEvent.Post.BUS.addListener((orig) -> handler.handle(Minecraft.getInstance().level)));

        events.registerEvent(ConnectedToServerEvent.class, priority -> ClientPlayerNetworkEvent.LoggingIn.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ConnectedToServerEvent event = new ConnectedToServerEvent(Minecraft.getInstance());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(DisconnectedFromServerEvent.class, priority -> ClientPlayerNetworkEvent.LoggingOut.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final DisconnectedFromServerEvent event = new DisconnectedFromServerEvent(Minecraft.getInstance());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ScreenDrawEvent.Pre.class, priority -> ScreenEvent.Render.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenDrawEvent.Pre event = new ScreenDrawEvent.Pre(orig.getScreen(),
                    orig.getGuiGraphics(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getPartialTick());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ContainerScreenDrawEvent.Background.class, priority -> ContainerScreenEvent.Render.Background.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ContainerScreenDrawEvent.Background event = new ContainerScreenDrawEvent.Background(orig.getContainerScreen(),
                    orig.getGuiGraphics(),
                    orig.getMouseX(),
                    orig.getMouseY());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ContainerScreenDrawEvent.Foreground.class, priority -> ContainerScreenEvent.Render.Foreground.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ContainerScreenDrawEvent.Foreground event = new ContainerScreenDrawEvent.Foreground(orig.getContainerScreen(),
                    orig.getGuiGraphics(),
                    orig.getMouseX(),
                    orig.getMouseY());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ScreenDrawEvent.Post.class, priority -> ScreenEvent.Render.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenDrawEvent.Post event = new ScreenDrawEvent.Post(orig.getScreen(),
                    orig.getGuiGraphics(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getPartialTick());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ScreenMouseEvent.Click.Pre.class, priority -> ScreenEvent.MouseButtonPressed.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenMouseEvent.Click.Pre event = new ScreenMouseEvent.Click.Pre(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ScreenMouseEvent.Click.Post.class, priority -> ScreenEvent.MouseButtonPressed.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenMouseEvent.Click.Post event = new ScreenMouseEvent.Click.Post(orig.getScreen(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getButton());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ScreenMouseEvent.Drag.Pre.class, priority -> ScreenEvent.MouseDragged.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenMouseEvent.Drag.Pre event = new ScreenMouseEvent.Drag.Pre(orig.getScreen(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getMouseButton(),
                    orig.getDragX(),
                    orig.getDragY());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ScreenMouseEvent.Drag.Post.class, priority -> ScreenEvent.MouseDragged.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenMouseEvent.Drag.Post event = new ScreenMouseEvent.Drag.Post(orig.getScreen(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getMouseButton(),
                    orig.getDragX(),
                    orig.getDragY());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ScreenMouseEvent.Release.Pre.class, priority -> ScreenEvent.MouseButtonReleased.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenMouseEvent.Release.Pre event = new ScreenMouseEvent.Release.Pre(orig.getScreen(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getButton());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ScreenMouseEvent.Release.Post.class, priority -> ScreenEvent.MouseButtonReleased.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenMouseEvent.Release.Post event = new ScreenMouseEvent.Release.Post(orig.getScreen(),
                    orig.getMouseX(),
                    orig.getMouseY(),
                    orig.getButton());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(ScreenKeyEvent.Press.Pre.class, priority -> ScreenEvent.KeyPressed.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenKeyEvent.Press.Pre event = new ScreenKeyEvent.Press.Pre(orig.getScreen(),
                    orig.getKeyCode(),
                    orig.getScanCode(),
                    orig.getModifiers());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ScreenKeyEvent.Press.Post.class, priority -> ScreenEvent.KeyPressed.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenKeyEvent.Press.Post event = new ScreenKeyEvent.Press.Post(orig.getScreen(),
                    orig.getKeyCode(),
                    orig.getScanCode(),
                    orig.getModifiers());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ScreenKeyEvent.Release.Pre.class, priority -> ScreenEvent.KeyReleased.Pre.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenKeyEvent.Release.Pre event = new ScreenKeyEvent.Release.Pre(orig.getScreen(),
                    orig.getKeyCode(),
                    orig.getScanCode(),
                    orig.getModifiers());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(ScreenKeyEvent.Release.Post.class, priority -> ScreenEvent.KeyReleased.Post.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ScreenKeyEvent.Release.Post event = new ScreenKeyEvent.Release.Post(orig.getScreen(),
                    orig.getKeyCode(),
                    orig.getScanCode(),
                    orig.getModifiers());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(FovUpdateEvent.class, priority -> ComputeFovModifierEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (ComputeFovModifierEvent orig) -> {
            final FovUpdateEvent event = new FovUpdateEvent(orig.getPlayer(), orig.getFovModifier());
            events.fireEventHandlers(priority, event);
            if (event.getFov() != null) {
                orig.setNewFovModifier(event.getFov());
            }
        }));

        events.registerEvent(ItemTooltipEvent.class, priority -> net.minecraftforge.event.entity.player.ItemTooltipEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final ItemTooltipEvent event = new ItemTooltipEvent(orig.getItemStack(), orig.getEntity(), orig.getToolTip(), orig.getFlags());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(UseItemInputEvent.class, priority -> InputEvent.InteractionKeyMappingTriggered.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            if (orig.isUseItem()) {
                final UseItemInputEvent event = new UseItemInputEvent(orig.getHand());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setSwingHand(false);
                    return true;
                }
            }
            return false;
        }));

        events.registerEvent(RenderHandEvent.class, priority -> net.minecraftforge.client.event.RenderHandEvent.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final RenderHandEvent event = new RenderHandEvent(orig.getHand(), orig.getItemStack(), orig.getSwingProgress());
            events.fireEventHandlers(priority, event);
            return event.isCanceled();
        }));

        events.registerEvent(KeyInputEvent.class, priority -> InputEvent.Key.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final KeyInputEvent event = new KeyInputEvent(orig.getKey(), orig.getScanCode(), orig.getAction(), orig.getModifiers());
            events.fireEventHandlers(priority, event);
        }));

        events.registerEvent(OpenScreenEvent.class, priority -> ScreenEvent.Opening.BUS.addListener(ForgeBalmEvents.toForge(priority), (orig) -> {
            final OpenScreenEvent event = new OpenScreenEvent(orig.getScreen());
            events.fireEventHandlers(priority, event);
            if (event.getNewScreen() != null) {
                orig.setNewScreen(event.getNewScreen());
            }
            return event.isCanceled();
        }));
    }
}
