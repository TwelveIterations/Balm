package net.blay09.mods.balm.client.platform.config.screen.list;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.client.platform.config.screen.list.internal.BalmConfigListDragHandleButton;
import net.blay09.mods.balm.client.platform.config.screen.list.internal.BalmConfigListEditorValue;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredList;
import net.blay09.mods.balm.platform.config.schema.ConfiguredSet;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public abstract class BalmConfigListEditorEntry<T> extends ContainerObjectSelectionList.Entry<BalmConfigListEditorEntry<T>> {
    protected static final int ACTION_BUTTON_WIDTH = 60;
    protected static final int ACTION_BUTTON_HEIGHT = 20;
    protected static final int ACTION_BUTTON_SPACING = 4;

    protected final BalmConfigListEditorContext<T> context;
    private final BalmConfigListDragHandleButton dragHandleButton;
    protected final List<AbstractWidget> children = new ArrayList<>();
    private final List<AbstractWidget> actionWidgets = new ArrayList<>();
    protected final BalmConfigListEditorValue<T> valueHolder;

    protected BalmConfigListEditorEntry(BalmConfigListEditorContext<T> context, BalmConfigListEditorValue<T> valueHolder) {
        this.context = context;
        this.valueHolder = valueHolder;
        dragHandleButton = new BalmConfigListDragHandleButton(context.dragController(), this, () -> getMessage());
        if (context.canReorderValues()) {
            children.add(dragHandleButton);
        }
    }

    public BalmConfigListEditorValue<T> valueHolder() {
        return valueHolder;
    }

    @Override
    public List<? extends AbstractWidget> children() {
        return children;
    }

    @Override
    public List<? extends AbstractWidget> narratables() {
        return children;
    }

    public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
        children.forEach(widgetVisitor);
    }

    @Override
    public final void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
        if (context instanceof BalmConfigListEditorScreen<T> screen && screen.isDragging(this)) {
            final var list = screen.list();
            if (list == null || !list.isExtractingDraggedEntry()) {
                return;
            }

            final var pose = graphics.pose();
            pose.pushMatrix();
            pose.translate(0, (float) list.draggedEntryYOffset(this));
            extractEntryContent(graphics, mouseX, mouseY, hovered, partialTick);
            extractActions(graphics, mouseX, mouseY, partialTick);
            pose.popMatrix();
            return;
        }

        extractEntryContent(graphics, mouseX, mouseY, hovered, partialTick);
        extractActions(graphics, mouseX, mouseY, partialTick);
    }

    protected abstract void extractEntryContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick);

    private void extractActions(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        layoutActions();
        if (context.canReorderValues()) {
            dragHandleButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }
        actionWidgets.forEach(widget -> widget.extractRenderState(graphics, mouseX, mouseY, partialTick));
    }

    protected int getContentLeftAfterDragHandle() {
        return context.canReorderValues() ? getContentX() + BalmConfigListDragHandleButton.WIDTH + ACTION_BUTTON_SPACING : getContentX();
    }

    protected int getContentRightBeforeActions() {
        final var actionsWidth = getActionWidgetsWidth();
        return actionsWidth > 0 ? getContentRight() - actionsWidth - ACTION_BUTTON_SPACING : getContentRight();
    }

    protected void addActionWidget(AbstractWidget widget) {
        actionWidgets.add(widget);
        children.add(widget);
    }

    public void startEditing(@Nullable Object initialValue) {
    }

    public void stopEditing() {
    }

    public DataResult<?> validate(ConfigControlBinding<? extends Collection<T>> binding) {
        final var value = valueHolder.value();
        if (value == null) {
            return DataResult.success(null);
        }

        return validateElement(binding, value);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected DataResult<T> validateElement(ConfigControlBinding<? extends Collection<T>> binding, T value) {
        return switch (binding.property()) {
            case ConfiguredList<?> configuredList -> ((ConfiguredList<T>) configuredList).validateElement(value);
            case ConfiguredSet<?> configuredSet -> ((ConfiguredSet<T>) configuredSet).validateElement(value);
            default -> ((ConfigControlBinding) binding).validateValue(value);
        };
    }

    private int getActionWidgetsWidth() {
        return actionWidgets.stream().filter(widget -> widget.visible)
                .mapToInt(AbstractWidget::getWidth)
                .map(it -> it + ACTION_BUTTON_SPACING)
                .sum();
    }

    private void layoutActions() {
        updateActionWidgets();
        final int y = getContentY() + (getContentHeight() - ACTION_BUTTON_HEIGHT) / 2;
        if (context.canReorderValues()) {
            dragHandleButton.setX(getContentX());
            dragHandleButton.setY(y);
        }
        int x = getContentRight();
        for (int i = actionWidgets.size() - 1; i >= 0; i--) {
            final var widget = actionWidgets.get(i);
            if (widget.visible) {
                x -= widget.getWidth();
                widget.setX(x);
                widget.setY(y);
                x -= ACTION_BUTTON_SPACING;
            }
        }
    }

    protected void updateActionWidgets() {
    }

    protected Component getMessage() {
        return Component.literal(String.valueOf(valueHolder.value()));
    }

}
