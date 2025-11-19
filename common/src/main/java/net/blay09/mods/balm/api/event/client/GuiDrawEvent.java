package net.blay09.mods.balm.api.event.client;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.GuiGraphics;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.RenderCallback.Gui} instead.
 */
@Deprecated
public abstract class GuiDrawEvent extends BalmEvent {

    private final Window window;
    private final GuiGraphics guiGraphics;
    private final Element element;
    public GuiDrawEvent(Window window, GuiGraphics guiGraphics, Element element) {
        this.window = window;
        this.guiGraphics = guiGraphics;
        this.element = element;
    }

    public Window getWindow() {
        return window;
    }

    public GuiGraphics getGuiGraphics() {
        return guiGraphics;
    }

    public Element getElement() {
        return element;
    }

    public enum Element {
        ALL,
        HEALTH,
        CHAT,
        DEBUG,
        BOSS_INFO,
        PLAYER_LIST
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.RenderCallback.Gui} instead.
     */
    @Deprecated
    public static class Pre extends GuiDrawEvent {
        public Pre(Window window, GuiGraphics guiGraphics, Element element) {
            super(window, guiGraphics, element);
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.RenderCallback.Gui} instead.
     */
    @Deprecated
    public static class Post extends GuiDrawEvent {
        public Post(Window window, GuiGraphics guiGraphics, Element element) {
            super(window, guiGraphics, element);
        }
    }
}
