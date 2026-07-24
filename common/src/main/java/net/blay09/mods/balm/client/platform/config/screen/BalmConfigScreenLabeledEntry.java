package net.blay09.mods.balm.client.platform.config.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BalmConfigScreenLabeledEntry extends BalmConfigScreenEntry {

    private static final int LABEL_WIDTH = 170;
    private static final int LABEL_MARGIN_X = 12;
    private static final int TOOLTIP_MAX_WIDTH = 200;

    protected final Font font;
    private final List<FormattedCharSequence> label;
    private final Component tooltip;
    private final AbstractWidget control;
    private final List<AbstractWidget> children;

    protected BalmConfigScreenLabeledEntry(BalmConfigScreenContext context, Component label, Component tooltip, AbstractWidget control) {
        super(context);
        this.font = context.font();
        this.label = font.split(label, LABEL_WIDTH);
        this.tooltip = tooltip;
        this.control = control;
        this.children = List.of(control);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return children;
    }

    public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
        children.forEach(widgetVisitor);
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
        final var entryX = getEntryX();
        extractLabel(graphics, entryX, getContentY());

        final var controlX = entryX + LABEL_WIDTH + LABEL_MARGIN_X;
        extractControl(graphics, controlX, getContentY(), mouseX, mouseY, partialTick);

        final var error = getValidationError();
        if (error != null) {
            extractValidationError(graphics, error, controlX - 12, getContentY() + 5);
        }
    }

    private void extractControl(GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        control.setX(x);
        control.setY(y);
        control.setTooltip(Tooltip.create(Objects.requireNonNullElse(getValidationError(), tooltip)));
        control.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    private int getEntryX() {
        final var entryWidth = LABEL_WIDTH + LABEL_MARGIN_X + control.getWidth();
        return getContentX() + Math.max(0, (getContentWidth() - entryWidth) / 2);
    }

    protected abstract @Nullable Component getValidationError();

    protected void extractLabel(GuiGraphicsExtractor graphics, int x, int y) {
        if (label.size() == 1) {
            graphics.text(font, label.getFirst(), x, y + 5, 0xFFFFFFFF);
        } else if (label.size() >= 2) {
            graphics.text(font, label.get(0), x, y, 0xFFFFFFFF);
            graphics.text(font, label.get(1), x, y + 10, 0xFFFFFFFF);
        }
    }

    protected void extractValidationError(GuiGraphicsExtractor graphics, Component error, int x, int y) {
        graphics.text(font, Component.literal("!").withColor(TextColor.DARK_RED), x, y + 2, 0xFFFFFFFF);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        // This is a workaround to avoid breaking focus on nested container widgets,
        // where unfocusing and refocusing would unfocus the parent control
        if (getFocused() != focused) {
            super.setFocused(focused);
        }
    }

}
