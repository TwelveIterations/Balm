package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public abstract class ScreenDrawEvent extends BalmEvent {
    private final Screen screen;
    private final GuiGraphics guiGraphics;
    private final int mouseX;
    private final int mouseY;
    private final float tickDelta;

    public ScreenDrawEvent(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        this.screen = screen;
        this.guiGraphics = guiGraphics;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.tickDelta = tickDelta;
    }

    public Screen getScreen() {
        return screen;
    }

    public GuiGraphics getGuiGraphics() {
        return guiGraphics;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public static class Pre extends ScreenDrawEvent {
        public Pre(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
            super(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    }

    public static class Post extends ScreenDrawEvent {
        public Post(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
            super(screen, guiGraphics, mouseX, mouseY, tickDelta);
        }
    }
}
