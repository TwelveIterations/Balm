package net.blay09.mods.balm.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SegmentedProgressRenderer implements ProgressRenderer {
    private final List<ProgressRenderer> segments = new ArrayList<>();
    private final List<Float> segmentWeights = new ArrayList<>();
    private final Identifier texture;
    private final int textureWidth;
    private final int textureHeight;
    private int totalLength;

    public SegmentedProgressRenderer(Identifier texture, int textureWidth, int textureHeight) {
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public SegmentedProgressRenderer addHorizontalSegment(int x, int y, int width, int height, int textureU, int textureV) {
        addSegment(SimpleProgressRenderer.horizontal(texture, textureWidth, textureHeight).pos(x, y).size(width, height).uv(textureU, textureV));
        return this;
    }

    public SegmentedProgressRenderer addVerticalSegment(int x, int y, int width, int height, int textureU, int textureV) {
        addSegment(SimpleProgressRenderer.vertical(texture, textureWidth, textureHeight).pos(x, y).size(width, height).uv(textureU, textureV));
        return this;
    }

    public SegmentedProgressRenderer addReverseHorizontalSegment(int x, int y, int width, int height, int textureU, int textureV) {
        addSegment(SimpleProgressRenderer.reverseHorizontal(texture, textureWidth, textureHeight).pos(x, y).size(width, height).uv(textureU, textureV));
        return this;
    }

    public SegmentedProgressRenderer addReverseVerticalSegment(int x, int y, int width, int height, int textureU, int textureV) {
        addSegment(SimpleProgressRenderer.reverseVertical(texture, textureWidth, textureHeight).pos(x, y).size(width, height).uv(textureU, textureV));
        return this;
    }

    public SegmentedProgressRenderer addInvisibleSegment(int size) {
        addSegment(SimpleProgressRenderer.invisible().size(size, size));
        return this;
    }

    public SegmentedProgressRenderer addSegment(ProgressRenderer segment) {
        segments.add(segment);
        totalLength += segment.getLength();

        segmentWeights.clear();
        for (ProgressRenderer seg : segments) {
            segmentWeights.add((float) seg.getLength() / totalLength);
        }

        return this;
    }

    public SegmentedProgressRenderer addParallelSegments(ProgressRenderer... segments) {
        return addSegment(new ParallelProgressRenderer(segments));
    }

    @Override
    public int getLength() {
        return totalLength;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenX, int screenY, float progress) {
        if (progress <= 0 || segments.isEmpty()) {
            return;
        }

        var currentProgress = 0f;
        for (int i = 0; i < segments.size(); i++) {
            final var segment = segments.get(i);
            final var segmentWeight = segmentWeights.get(i);
            final var segmentStartProgress = currentProgress;

            if (progress > segmentStartProgress) {
                final var segmentProgress = Math.min((progress - segmentStartProgress) / segmentWeight, 1f);
                segment.render(guiGraphics, screenX, screenY, segmentProgress);
            }

            currentProgress += segmentWeight;
        }
    }
}
