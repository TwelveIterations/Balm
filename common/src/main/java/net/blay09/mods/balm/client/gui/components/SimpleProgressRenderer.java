package net.blay09.mods.balm.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SimpleProgressRenderer implements ProgressRenderer {

    public enum Direction {
        HORIZONTAL,
        VERTICAL,
        REVERSE_HORIZONTAL,
        REVERSE_VERTICAL,
        INVISIBLE
    }

    private final Direction direction;
    private final ResourceLocation texture;
    private final int textureWidth;
    private final int textureHeight;
    private int x;
    private int y;
    private int width;
    private int height;
    private int textureU;
    private int textureV;

    public SimpleProgressRenderer(ResourceLocation texture, int textureWidth, int textureHeight, Direction direction) {
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.direction = direction;
    }

    public SimpleProgressRenderer pos(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public SimpleProgressRenderer size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public SimpleProgressRenderer uv(int u, int v) {
        this.textureU = u;
        this.textureV = v;
        return this;
    }

    public static SimpleProgressRenderer horizontal(ResourceLocation texture, int textureWidth, int textureHeight) {
        return new SimpleProgressRenderer(texture, textureWidth, textureHeight, Direction.HORIZONTAL);
    }

    public static SimpleProgressRenderer vertical(ResourceLocation texture, int textureWidth, int textureHeight) {
        return new SimpleProgressRenderer(texture, textureWidth, textureHeight, Direction.VERTICAL);
    }

    public static SimpleProgressRenderer reverseHorizontal(ResourceLocation texture, int textureWidth, int textureHeight) {
        return new SimpleProgressRenderer(texture, textureWidth, textureHeight, Direction.REVERSE_HORIZONTAL);
    }

    public static SimpleProgressRenderer reverseVertical(ResourceLocation texture, int textureWidth, int textureHeight) {
        return new SimpleProgressRenderer(texture, textureWidth, textureHeight, Direction.REVERSE_VERTICAL);
    }

    public static SimpleProgressRenderer invisible() {
        return new SimpleProgressRenderer(null, 0, 0, Direction.INVISIBLE);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenX, int screenY, float progress) {
        if (progress <= 0 || direction == Direction.INVISIBLE) {
            return;
        }

        final var renderX = screenX + x;
        final var renderY = screenY + y;

        switch (direction) {
            case HORIZONTAL -> {
                final var progressWidth = (int) (progress * width);
                if (progressWidth > 0) {
                    guiGraphics.blit(texture, renderX, renderY, textureU, textureV, progressWidth, height, textureWidth, textureHeight);
                }
            }
            case VERTICAL -> {
                final var progressHeight = (int) (progress * height);
                if (progressHeight > 0) {
                    guiGraphics.blit(texture, renderX, renderY, textureU, textureV, width, progressHeight, textureWidth, textureHeight);
                }
            }
            case REVERSE_HORIZONTAL -> {
                final var progressWidth = (int) (progress * width);
                if (progressWidth > 0) {
                    int startX = renderX + width - progressWidth;
                    guiGraphics.blit(texture, startX, renderY, textureU + width - progressWidth, textureV, progressWidth, height, textureWidth, textureHeight);
                }
            }
            case REVERSE_VERTICAL -> {
                final var progressHeight = (int) (progress * height);
                if (progressHeight > 0) {
                    int startY = renderY + height - progressHeight;
                    guiGraphics.blit(texture, renderX, startY, textureU, textureV + height - progressHeight, width, progressHeight, textureWidth, textureHeight);
                }
            }
        }
    }

    @Override
    public int getLength() {
        return switch (direction) {
            case HORIZONTAL, REVERSE_HORIZONTAL -> width;
            case VERTICAL, REVERSE_VERTICAL -> height;
            case INVISIBLE -> Math.max(width, height);
        };
    }

    public ResourceLocation texture() {
        return texture;
    }

    public int textureWidth() {
        return textureWidth;
    }

    public int textureHeight() {
        return textureHeight;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int textureU() {
        return textureU;
    }

    public int textureV() {
        return textureV;
    }

    public Direction direction() {
        return direction;
    }

}
