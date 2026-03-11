package net.blay09.mods.balm.client.gui.components;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface ProgressRenderer {
    int getLength();

    void render(GuiGraphicsExtractor guiGraphics, int x, int y, float progress);
}

