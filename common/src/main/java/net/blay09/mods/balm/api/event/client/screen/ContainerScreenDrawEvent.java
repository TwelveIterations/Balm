package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public abstract class ContainerScreenDrawEvent extends BalmEvent {
    private final Screen screen;
    private final GuiGraphics guiGraphics;
    private final int mouseX;
    private final int mouseY;

    public ContainerScreenDrawEvent(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.screen = screen;
        this.guiGraphics = guiGraphics;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
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

    public static class Background extends ContainerScreenDrawEvent {
        public Background(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
            super(screen, guiGraphics, mouseX, mouseY);
        }
    }

    public static class Foreground extends ContainerScreenDrawEvent {
        public Foreground(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
            super(screen, guiGraphics, mouseX, mouseY);
        }
    }
}
