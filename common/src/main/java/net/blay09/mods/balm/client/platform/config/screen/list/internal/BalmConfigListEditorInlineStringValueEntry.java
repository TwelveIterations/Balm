package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorContext;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorEntry;
import net.blay09.mods.balm.platform.config.internal.PrimitiveConfigCodecs;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class BalmConfigListEditorInlineStringValueEntry<T> extends BalmConfigListEditorEntry<T> {
    private static final Component EDIT_LABEL = Component.translatable("gui.balm.configuration.edit");
    private static final Component DELETE_LABEL = Component.translatable("gui.balm.configuration.delete");
    private static final Component RESET_LABEL = Component.translatable("gui.balm.configuration.reset");
    private static final String ELLIPSIS = "...";
    private static final int EDIT_BOX_HEIGHT = 20;

    private final Button editButton;
    private final Button doneButton;
    private final Button deleteButton;
    private final Button resetButton;
    private final Component label;
    private @Nullable EditBox editBox;

    public BalmConfigListEditorInlineStringValueEntry(BalmConfigListEditorContext<T> context, BalmConfigListEditorValue<T> value) {
        this(context, value, Component.literal(value.value() != null ? String.valueOf(value.value()) : ""));
    }

    public BalmConfigListEditorInlineStringValueEntry(BalmConfigListEditorContext<T> context, BalmConfigListEditorValue<T> value, Component label) {
        super(context, value);
        this.label = label;
        editButton = Button.builder(EDIT_LABEL, this::onEditButton)
                .size(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT)
                .createNarration(it -> CommonComponents.joinForNarration(label, it.get()))
                .build();
        doneButton = Button.builder(CommonComponents.GUI_DONE, this::onDoneButton)
                .size(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT)
                .createNarration(it -> CommonComponents.joinForNarration(label, it.get()))
                .build();
        deleteButton = Button.builder(DELETE_LABEL, this::onDeleteButton)
                .size(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT)
                .createNarration(it -> CommonComponents.joinForNarration(label, it.get()))
                .build();
        resetButton = Button.builder(RESET_LABEL, this::onResetButton)
                .size(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT)
                .createNarration(it -> CommonComponents.joinForNarration(label, it.get()))
                .build();
        addActionWidget(editButton);
        addActionWidget(doneButton);
        addActionWidget(deleteButton);
        addActionWidget(resetButton);
        if (valueHolder.entryState() instanceof EditState) {
            ensureEditBox();
        }
    }

    @Override
    public void startEditing(@Nullable Object initialValue) {
        final var value = valueHolder.value();
        final var initialText = initialValue != null ? String.valueOf(initialValue) : value != null ? String.valueOf(value) : "";
        valueHolder.entryState(new EditState(initialText));
        final var editBox = ensureEditBox();
        editBox.setValue(initialText);
        focusEditBox(editBox);
    }

    @Override
    public void stopEditing() {
        if (editBox != null) {
            editBox.setFocused(false);
            children.remove(editBox);
            editBox = null;
        }

        valueHolder.entryState(null);
        setFocused(null);
    }

    public String getPendingValue() {
        return editBox != null ? editBox.getValue() : String.valueOf(valueHolder.value());
    }

    public boolean isEditing() {
        return editBox != null || valueHolder.entryState() instanceof EditState;
    }

    @Override
    public DataResult<?> validate(ConfigControlBinding<? extends Collection<T>> binding) {
        if (valueHolder.entryState() instanceof EditState(String value)) {
            return parseValue(value);
        }

        return super.validate(binding);
    }

    public void setValue(String value) {
        valueHolder.entryState(new EditState(value));
        parseValue(value).ifSuccess(validValue -> {
            valueHolder.value(validValue);
            context.revalidate();
        }).ifError(error -> context.setValidationError(this, Component.literal(error.message())));
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (isEditing() && event.isConfirmation()) {
            if (canCommit()) {
                stopEditing();
                context.commit();
            }
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (focused == editButton && editBox != null) {
            super.setFocused(editBox);
            return;
        }

        super.setFocused(focused);
    }

    @Override
    protected void extractEntryContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
        if (isEditing()) {
            ensureEditBox().extractRenderState(graphics, mouseX, mouseY, partialTick);
        } else {
            final var x = getContentLeftAfterDragHandle();
            final var maxWidth = getContentRightBeforeActions() - x;
            if (maxWidth > 0) {
                graphics.text(context.font(), truncateLabel(label, maxWidth), x, getContentY() + 5, 0xFFFFFFFF);
            }
        }
    }

    private Component truncateLabel(Component label, int maxWidth) {
        final var font = context.font();
        if (font.width(label) <= maxWidth) {
            return label;
        }

        final var ellipsisWidth = font.width(ELLIPSIS);
        if (maxWidth <= ellipsisWidth) {
            return Component.literal(font.plainSubstrByWidth(ELLIPSIS, maxWidth));
        }

        final var truncatedLabel = font.plainSubstrByWidth(label.getString(), maxWidth - ellipsisWidth);
        return Component.literal(truncatedLabel + ELLIPSIS);
    }

    @Override
    protected void updateActionWidgets() {
        final var editing = isEditing();
        editButton.visible = !editing;
        editButton.active = !editing && canEdit();
        doneButton.visible = editing;
        doneButton.active = editing && canCommit();
        deleteButton.visible = !editing;
        deleteButton.active = !editing && canDelete();
        resetButton.visible = editing;
        resetButton.active = editing && canReset();
    }

    public boolean canCommit() {
        return parseValue(getPendingValue()).isSuccess();
    }

    @SuppressWarnings("unchecked")
    private DataResult<T> parseValue(String value) {
        return PrimitiveConfigCodecs.parse((ConfiguredProperty<T>) context.property(), value);
    }

    protected void onDoneButton(Button button) {
        if (canCommit()) {
            stopEditing();
            context.commit();
        }
    }

    public boolean canEdit() {
        return true;
    }

    protected void onEditButton(Button button) {
        if (canEdit()) {
            startEditing(null);
        }
    }

    public boolean canDelete() {
        return true;
    }

    protected void onDeleteButton(Button button) {
        if (canDelete()) {
            context.delete(this);
        }
    }

    public boolean canReset() {
        return valueHolder.originalValue() != null &&
                (!Objects.equals(valueHolder.value(), valueHolder.originalValue()) || !canCommit());
    }

    protected void onResetButton(Button button) {
        if (canReset()) {
            valueHolder.reset();
            final var resetValue = valueHolder.value();
            final var resetText = resetValue != null ? String.valueOf(resetValue) : "";
            valueHolder.entryState(new EditState(resetText));
            if (editBox != null) {
                editBox.setValue(resetText);
            }
        }
    }

    protected EditBox ensureEditBox() {
        if (editBox == null) {
            editBox = new EditBox(context.font(), EDIT_LABEL);
            editBox.setMaxLength(2048);
            editBox.setValue(getPendingValue());
            editBox.setResponder(this::setValue);
            children.addFirst(editBox);
        }

        final var x = getContentLeftAfterDragHandle();
        editBox.setX(x);
        editBox.setY(getContentY() + (getContentHeight() - EDIT_BOX_HEIGHT) / 2);
        editBox.setWidth(Math.max(1, getContentRightBeforeActions() - x));
        editBox.setHeight(EDIT_BOX_HEIGHT);
        return editBox;
    }

    private void focusEditBox(EditBox editBox) {
        editBox.setFocused(true);
        setFocused(editBox);
        context.focusEntry(this);
    }

    protected record EditState(String value) {
    }
}
