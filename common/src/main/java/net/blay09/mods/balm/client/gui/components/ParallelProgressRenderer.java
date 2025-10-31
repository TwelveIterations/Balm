package net.blay09.mods.balm.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;

import java.util.Arrays;

public class ParallelProgressRenderer implements ProgressRenderer {

    private final ProgressRenderer[] renderers;
    private final int length;

    public ParallelProgressRenderer(ProgressRenderer... renderers) {
        this.renderers = renderers;
        this.length = Arrays.stream(renderers).mapToInt(ProgressRenderer::getLength).max().orElse(0);
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float progress) {
        for (ProgressRenderer renderer : renderers) {
            renderer.render(guiGraphics, x, y, progress);
        }
    }
}
