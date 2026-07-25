package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BalmConfigListDragHandleButton extends Button {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 20;

    private static final Identifier ICON = Identifier.fromNamespaceAndPath("balm", "widgets/drag_handle");
    private static final Identifier HIGHLIGHTED_ICON = Identifier.fromNamespaceAndPath("balm", "widgets/drag_handle_highlighted");
    private static final Component LABEL = Component.translatable("gui.balm.configuration.list.drag_handle");
    private static final Component INACTIVE_TOOLTIP = Component.translatable("gui.balm.configuration.list.drag_to_reorder");
    private static final Component ACTIVE_TOOLTIP = Component.translatable("gui.balm.configuration.list.drag_to_reorder.active");

    private final BalmConfigListDragController list;
    private final Object entry;
    private boolean keyboardDragActive;

    public BalmConfigListDragHandleButton(BalmConfigListDragController list, Object entry) {
        super(0, 0, WIDTH, HEIGHT, LABEL, _ -> {
        }, Button.DEFAULT_NARRATION);
        this.list = list;
        this.entry = entry;
        updateTooltip();
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        if (doubleClick) {
            list.stopDragging();
            if (event.hasShiftDown()) {
                list.moveToBottom(entry);
            } else {
                list.moveToTop(entry);
            }
            return;
        }

        list.startDragging(entry, event.y());
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dragX, double dragY) {
        list.dragTo(event.y());
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == InputConstants.KEY_SPACE || event.key() == InputConstants.KEY_RETURN) {
            keyboardDragActive = !keyboardDragActive;
            updateTooltip();
            return true;
        }

        if (!keyboardDragActive) {
            return super.keyPressed(event);
        }

        return switch (event.key()) {
            case InputConstants.KEY_UP -> {
                list.moveUp(entry);
                yield true;
            }
            case InputConstants.KEY_DOWN -> {
                list.moveDown(entry);
                yield true;
            }
            case InputConstants.KEY_HOME -> {
                list.moveToTop(entry);
                yield true;
            }
            case InputConstants.KEY_END -> {
                list.moveToBottom(entry);
                yield true;
            }
            default -> super.keyPressed(event);
        };
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        list.stopDragging();
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            keyboardDragActive = false;
            updateTooltip();
        }
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        final var icon = list.isDragging(entry) || keyboardDragActive || isHoveredOrFocused() ? HIGHLIGHTED_ICON : ICON;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, getX(), getY() + 2, WIDTH, 16);
    }

    private void updateTooltip() {
        setTooltip(Tooltip.create(keyboardDragActive ? ACTIVE_TOOLTIP : INACTIVE_TOOLTIP));
    }
}
