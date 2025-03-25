package net.blay09.mods.balm.neoforge.event;

import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.blay09.mods.balm.api.event.client.RenderHandEvent;
import net.blay09.mods.balm.api.event.client.*;
import net.blay09.mods.balm.api.event.client.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.Nullable;

public class NeoForgeBalmClientEvents {

    public static void registerEvents(NeoForgeBalmEvents events) {
        events.registerTickEvent(TickType.Client, TickPhase.Start, (ClientTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre orig) -> {
                handler.handle(Minecraft.getInstance());
            });
        });
        events.registerTickEvent(TickType.Client, TickPhase.End, (ClientTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post orig) -> {
                handler.handle(Minecraft.getInstance());
            });
        });
        events.registerTickEvent(TickType.ClientLevel, TickPhase.Start, (ClientLevelTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((LevelTickEvent.Pre orig) -> {
                if (orig.getLevel() instanceof ClientLevel clientLevel) {
                    handler.handle(clientLevel);
                }
            });
        });
        events.registerTickEvent(TickType.ClientLevel, TickPhase.End, (ClientLevelTickHandler handler) -> {
            NeoForge.EVENT_BUS.addListener((LevelTickEvent.Post orig) -> {
                if (orig.getLevel() instanceof ClientLevel clientLevel) {
                    handler.handle(clientLevel);
                }
            });
        });

        events.registerEvent(ClientStartedEvent.class, priority -> {
            ModLoadingContext.get().getActiveContainer().getEventBus().addListener(NeoForgeBalmEvents.toForge(priority), (FMLLoadCompleteEvent orig) -> {
                orig.enqueueWork(() -> {
                    final ClientStartedEvent event = new ClientStartedEvent(Minecraft.getInstance());
                    events.fireEventHandlers(priority, event);
                });
            });
        });

        events.registerEvent(ConnectedToServerEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ClientPlayerNetworkEvent.LoggingIn orig) -> {
                final ConnectedToServerEvent event = new ConnectedToServerEvent(Minecraft.getInstance());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(DisconnectedFromServerEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ClientPlayerNetworkEvent.LoggingOut orig) -> {
                final DisconnectedFromServerEvent event = new DisconnectedFromServerEvent(Minecraft.getInstance());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenInitEvent.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.Init.Pre orig) -> {
                final ScreenInitEvent.Pre event = new ScreenInitEvent.Pre(orig.getScreen());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenInitEvent.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.Init.Post orig) -> {
                final ScreenInitEvent.Post event = new ScreenInitEvent.Post(orig.getScreen());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenDrawEvent.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.Render.Pre orig) -> {
                final ScreenDrawEvent.Pre event = new ScreenDrawEvent.Pre(orig.getScreen(),
                        orig.getGuiGraphics(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getPartialTick());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ContainerScreenDrawEvent.Background.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ContainerScreenEvent.Render.Background orig) -> {
                final ContainerScreenDrawEvent.Background event = new ContainerScreenDrawEvent.Background(orig.getContainerScreen(),
                        orig.getGuiGraphics(),
                        orig.getMouseX(),
                        orig.getMouseY());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ContainerScreenDrawEvent.Foreground.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ContainerScreenEvent.Render.Foreground orig) -> {
                final ContainerScreenDrawEvent.Foreground event = new ContainerScreenDrawEvent.Foreground(orig.getContainerScreen(),
                        orig.getGuiGraphics(),
                        orig.getMouseX(),
                        orig.getMouseY());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenDrawEvent.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.Render.Post orig) -> {
                final ScreenDrawEvent.Post event = new ScreenDrawEvent.Post(orig.getScreen(),
                        orig.getGuiGraphics(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getPartialTick());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Click.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.MouseButtonPressed.Pre orig) -> {
                final ScreenMouseEvent.Click.Pre event = new ScreenMouseEvent.Click.Pre(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Click.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.MouseButtonPressed.Post orig) -> {
                final ScreenMouseEvent.Click.Post event = new ScreenMouseEvent.Click.Post(orig.getScreen(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setResult(ScreenEvent.MouseButtonPressed.Post.Result.FORCE_HANDLED);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Drag.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.MouseDragged.Pre orig) -> {
                final ScreenMouseEvent.Drag.Pre event = new ScreenMouseEvent.Drag.Pre(orig.getScreen(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getMouseButton(),
                        orig.getDragX(),
                        orig.getDragY());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Drag.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.MouseDragged.Post orig) -> {
                final ScreenMouseEvent.Drag.Post event = new ScreenMouseEvent.Drag.Post(orig.getScreen(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getMouseButton(),
                        orig.getDragX(),
                        orig.getDragY());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(ScreenMouseEvent.Release.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.MouseButtonReleased.Pre orig) -> {
                final ScreenMouseEvent.Release.Pre event = new ScreenMouseEvent.Release.Pre(orig.getScreen(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenMouseEvent.Release.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.MouseButtonReleased.Post orig) -> {
                final ScreenMouseEvent.Release.Post event = new ScreenMouseEvent.Release.Post(orig.getScreen(),
                        orig.getMouseX(),
                        orig.getMouseY(),
                        orig.getButton());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setResult(ScreenEvent.MouseButtonReleased.Post.Result.FORCE_HANDLED);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Press.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.KeyPressed.Pre orig) -> {
                final ScreenKeyEvent.Press.Pre event = new ScreenKeyEvent.Press.Pre(orig.getScreen(),
                        orig.getKeyCode(),
                        orig.getScanCode(),
                        orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Press.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.KeyPressed.Post orig) -> {
                final ScreenKeyEvent.Press.Post event = new ScreenKeyEvent.Press.Post(orig.getScreen(),
                        orig.getKeyCode(),
                        orig.getScanCode(),
                        orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Release.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.KeyReleased.Pre orig) -> {
                final ScreenKeyEvent.Release.Pre event = new ScreenKeyEvent.Release.Pre(orig.getScreen(),
                        orig.getKeyCode(),
                        orig.getScanCode(),
                        orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(ScreenKeyEvent.Release.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.KeyReleased.Post orig) -> {
                final ScreenKeyEvent.Release.Post event = new ScreenKeyEvent.Release.Post(orig.getScreen(),
                        orig.getKeyCode(),
                        orig.getScanCode(),
                        orig.getModifiers());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(FovUpdateEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ComputeFovModifierEvent orig) -> {
                final FovUpdateEvent event = new FovUpdateEvent(orig.getPlayer());
                events.fireEventHandlers(priority, event);
                if (event.getFov() != null) {
                    orig.setNewFovModifier(event.getFov());
                }
            });
        });

        events.registerEvent(ItemTooltipEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.event.entity.player.ItemTooltipEvent orig) -> {
                final ItemTooltipEvent event = new ItemTooltipEvent(orig.getItemStack(), orig.getEntity(), orig.getToolTip(), orig.getFlags());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(UseItemInputEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (InputEvent.InteractionKeyMappingTriggered orig) -> {
                if (orig.isUseItem()) {
                    final UseItemInputEvent event = new UseItemInputEvent(orig.getHand());
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setSwingHand(false);
                        orig.setCanceled(true);
                    }
                }
            });
        });

        events.registerEvent(RenderHandEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (net.neoforged.neoforge.client.event.RenderHandEvent orig) -> {
                final RenderHandEvent event = new RenderHandEvent(orig.getHand(), orig.getItemStack(), orig.getSwingProgress());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(KeyInputEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (InputEvent.Key orig) -> {
                final KeyInputEvent event = new KeyInputEvent(orig.getKey(), orig.getScanCode(), orig.getAction(), orig.getModifiers());
                events.fireEventHandlers(priority, event);
            });
        });

        events.registerEvent(BlockHighlightDrawEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (RenderHighlightEvent.Block orig) -> {
                final BlockHighlightDrawEvent event = new BlockHighlightDrawEvent(orig.getTarget(),
                        orig.getPoseStack(),
                        orig.getMultiBufferSource(),
                        orig.getCamera());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(OpenScreenEvent.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (ScreenEvent.Opening orig) -> {
                final OpenScreenEvent event = new OpenScreenEvent(orig.getScreen());
                events.fireEventHandlers(priority, event);
                if (event.getNewScreen() != null) {
                    orig.setNewScreen(event.getNewScreen());
                }
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
        });

        events.registerEvent(GuiDrawEvent.Pre.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (RenderGuiEvent.Pre orig) -> {
                final var window = Minecraft.getInstance().getWindow();
                final GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(window, orig.getGuiGraphics(), GuiDrawEvent.Element.ALL);
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });

            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (RenderGuiLayerEvent.Pre orig) -> {
                final var window = Minecraft.getInstance().getWindow();
                final var element = getGuiDrawEventElement(orig.getName());
                if (element != null) {
                    final GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(window, orig.getGuiGraphics(), element);
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setCanceled(true);
                    }
                }
            });
        });

        events.registerEvent(GuiDrawEvent.Post.class, priority -> {
            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (RenderGuiEvent.Post orig) -> {
                final var window = Minecraft.getInstance().getWindow();
                final GuiDrawEvent.Post event = new GuiDrawEvent.Post(window, orig.getGuiGraphics(), GuiDrawEvent.Element.ALL);
                events.fireEventHandlers(priority, event);
            });

            NeoForge.EVENT_BUS.addListener(NeoForgeBalmEvents.toForge(priority), (RenderGuiLayerEvent.Post orig) -> {
                final var window = Minecraft.getInstance().getWindow();
                final var element = getGuiDrawEventElement(orig.getName());
                if (element != null) {
                    final GuiDrawEvent.Post event = new GuiDrawEvent.Post(window, orig.getGuiGraphics(), element);
                    events.fireEventHandlers(priority, event);
                }
            });
        });
    }

    @Nullable
    private static GuiDrawEvent.Element getGuiDrawEventElement(ResourceLocation id) {
        GuiDrawEvent.Element type = null;
        if (id.equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            type = GuiDrawEvent.Element.HEALTH;
        } else if (id.equals(VanillaGuiLayers.CHAT)) {
            type = GuiDrawEvent.Element.CHAT;
        } else if (id.equals(VanillaGuiLayers.DEBUG_OVERLAY)) {
            type = GuiDrawEvent.Element.DEBUG;
        } else if (id.equals(VanillaGuiLayers.BOSS_OVERLAY)) {
            type = GuiDrawEvent.Element.BOSS_INFO;
        } else if (id.equals(VanillaGuiLayers.TAB_LIST)) {
            type = GuiDrawEvent.Element.PLAYER_LIST;
        }
        return type;
    }
}
