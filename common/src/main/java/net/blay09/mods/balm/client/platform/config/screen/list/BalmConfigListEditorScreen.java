package net.blay09.mods.balm.client.platform.config.screen.list;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.DataResult;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenContext;
import net.blay09.mods.balm.client.platform.config.screen.list.internal.*;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.ConfiguredSet;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BalmConfigListEditorScreen<T> extends Screen implements BalmConfigListEditorContext<T> {
    private static final Component SEARCH_LABEL = Component.translatable("gui.balm.configuration.search");
    private static final Component SEARCH_HINT = SEARCH_LABEL.copy().withStyle(EditBox.SEARCH_HINT_STYLE);
    private static final Component ADD_LABEL = Component.translatable("gui.balm.configuration.add");
    private static final Component ADD_BUTTON_LABEL = Component.literal("+");
    private static final int SEARCH_BOX_WIDTH = 200;
    private static final int SEARCH_BOX_HEIGHT = 15;
    private static final int DEFAULT_FOOTER_HEIGHT = 33;
    private static final int VALIDATION_ERROR_SPACING = 4;
    private static final int VALIDATION_ERROR_TOP_PADDING = 6;
    private static final int FOOTER_BUTTON_BOTTOM_PADDING = (DEFAULT_FOOTER_HEIGHT - Button.DEFAULT_HEIGHT) / 2;

    private final @Nullable Screen parent;
    private final BalmConfigScreenContext context;
    private final ConfigControlBinding<? extends Collection<T>> binding;
    private final BalmConfigListEditorState<T> state;
    private final HeaderAndFooterLayout layout;
    private final BiFunction<BalmConfigListEditorContext<T>, BalmConfigListEditorValue<T>, ? extends BalmConfigListEditorEntry<T>> entryFactory;
    private final BiPredicate<T, String> filterPredicate;

    private @Nullable EditBox searchBox;
    private @Nullable BalmConfigListEditorEntryList<T> list;
    private @Nullable FrameLayout footerLayout;
    private @Nullable StringWidget validationErrorWidget;
    private @Nullable Button addButton;
    private @Nullable Button doneButton;

    public BalmConfigListEditorScreen(@Nullable Screen parent,
                                      BalmConfigScreenContext context,
                                      ConfigControlBinding<? extends Collection<T>> binding,
                                      Component title,
                                      BiFunction<BalmConfigListEditorContext<T>, BalmConfigListEditorValue<T>, ? extends BalmConfigListEditorEntry<T>> entryFactory,
                                      BiPredicate<T, String> filterPredicate) {
        super(title);
        this.parent = parent;
        this.context = context;
        this.binding = binding;
        this.state = context.stateFor(binding.property()).getOrCreate(() -> BalmConfigListEditorState.wrap(binding.get()));
        this.layout = new HeaderAndFooterLayout(this, 36, DEFAULT_FOOTER_HEIGHT);
        this.entryFactory = entryFactory;
        this.filterPredicate = filterPredicate;
    }

    public static <T> BalmConfigListEditorScreenBuilder<T> builder(Screen parent, BalmConfigScreenContext context, ConfigControlBinding<? extends Collection<T>> binding) {
        return new BalmConfigListEditorScreenBuilderImpl<>(parent, context, binding);
    }

    @Override
    protected void init() {
        final var header = layout.addToHeader(LinearLayout.vertical().spacing(4));
        header.defaultCellSetting().alignHorizontallyCenter();
        header.addChild(new StringWidget(title, font));
        final var searchRow = header.addChild(LinearLayout.horizontal().spacing(4));
        searchBox = searchRow.addChild(new EditBox(font, SEARCH_BOX_WIDTH, SEARCH_BOX_HEIGHT, SEARCH_LABEL));
        searchBox.setHint(SEARCH_HINT);
        searchBox.setResponder(filter -> {
            refreshList(filter);
            if (list != null) {
                list.setScrollAmount(0);
            }
        });
        addButton = searchRow.addChild(Button.builder(ADD_BUTTON_LABEL, _ -> {
                    final var entryText = searchBox != null ? searchBox.getValue().trim() : "";
                    addEntry(entryText);
                })
                .size(SEARCH_BOX_HEIGHT, SEARCH_BOX_HEIGHT)
                .tooltip(Tooltip.create(ADD_LABEL))
                .createNarration(_ -> AbstractWidget.wrapDefaultNarrationMessage(ADD_LABEL))
                .build());

        list = layout.addToContents(new BalmConfigListEditorEntryList<>(this));
        refreshList();

        footerLayout = layout.addToFooter(new FrameLayout());
        validationErrorWidget = footerLayout.addChild(new StringWidget(Component.empty(), font),
                settings -> settings.align(0.5f, 0f).paddingTop(VALIDATION_ERROR_TOP_PADDING));
        doneButton = footerLayout.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> {
                    commit();
                    onClose();
                }).build(),
                settings -> settings.align(0.5f, 1f).paddingBottom(FOOTER_BUTTON_BOTTOM_PADDING));
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
        updateWidgets();
    }

    @Override
    public void onClose() {
        if (parent instanceof BalmConfigScreen configScreen) {
            configScreen.refreshControls();
        }
        minecraft.gui.setScreen(parent);
    }

    @Override
    protected void repositionElements() {
        layout.setFooterHeight(footerHeight());
        if (footerLayout != null) {
            footerLayout.setMinDimensions(width, footerHeight());
        }
        layout.arrangeElements();
        if (list != null) {
            list.updateSize(width, layout);
        }
    }

    @Override
    protected void setInitialFocus() {
        if (searchBox != null) {
            setInitialFocus(searchBox);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == InputConstants.KEY_F && event.hasControlDownWithQuirk() && !event.hasShiftDown() && !event.hasAltDown()) {
            focusSearchBox();
            return true;
        } else if (event.hasControlDownWithQuirk()
                && (event.key() == InputConstants.KEY_Z || event.key() == InputConstants.KEY_Y)
                && !event.hasShiftDown()
                && !event.hasAltDown()) {
            if (state.restoreDeletedValue()) {
                refreshList();
                revalidate();
            }
            return true;
        }

        return super.keyPressed(event);
    }

    private void focusSearchBox() {
        if (searchBox != null) {
            setInitialFocus(searchBox);
            searchBox.moveCursorToEnd(false);
            searchBox.setHighlightPos(0);
        }
    }

    public int contentHeight() {
        return layout.getContentHeight();
    }

    public int headerHeight() {
        return layout.getHeaderHeight();
    }

    public @Nullable BalmConfigListEditorEntryList<T> list() {
        return list;
    }

    @Override
    public BalmConfigListDragController dragController() {
        return Objects.requireNonNull(list);
    }

    public boolean isDragging(BalmConfigListEditorEntry<T> entry) {
        return list != null && list.isDragging(entry);
    }

    private void addEntry(String initialValue) {
        if (searchBox != null) {
            searchBox.setValue("");
        }

        if (list != null) {
            final var valueHolder = state.addValue();
            final var entry = entryFactory.apply(this, valueHolder);
            list.addEntry(entry);
            list.setScrollAmount(0);
            entry.startEditing(initialValue);
        }
    }

    @Override
    public Font font() {
        return getFont();
    }

    @Override
    public ConfiguredProperty<? extends Collection<T>> property() {
        return binding.property();
    }

    @Override
    public void focusEntry(BalmConfigListEditorEntry<T> entry) {
        if (list != null) {
            list.setSelected(entry);
            list.setFocused(entry);
            setFocused(list);
        }
    }

    @Override
    public void commit() {
        final var rawValues = state.rawValues();
        final var pendingCommitValues = binding.property() instanceof ConfiguredSet<?>
                ? new LinkedHashSet<>(rawValues)
                : List.copyOf(rawValues);
        if (validateValues(pendingCommitValues).isSuccess()) {
            setValues(pendingCommitValues);
            refreshList();
        }
    }

    private void refreshList() {
        refreshList(searchBox != null ? searchBox.getValue() : "");
    }

    private void refreshList(String filter) {
        if (list != null) {
            final var values = state.values();
            if (values.isEmpty()) {
                list.replaceEntries(List.of(new BalmConfigListEditorEmptyEntry<>(this)));
                return;
            }

            final var filteredEntries = new ArrayList<BalmConfigListEditorEntry<T>>();
            final var normalizedFilter = filter.trim().toLowerCase();
            for (final var valueHolder : values) {
                final var value = valueHolder.value();
                if (normalizedFilter.isEmpty() || value == null || filterPredicate.test(value, normalizedFilter)) {
                    filteredEntries.add(entryFactory.apply(this, valueHolder));
                }
            }
            list.replaceEntries(filteredEntries);
        }
    }

    @Override
    public boolean canReorderValues() {
        return !(binding.property() instanceof ConfiguredSet<?>) && !isSearchActive();
    }

    private boolean isSearchActive() {
        return searchBox != null && !searchBox.getValue().trim().isEmpty();
    }

    @Override
    public void delete(BalmConfigListEditorEntry<T> entry) {
        state.removeValue(entry.valueHolder);
        refreshList();
    }

    @Override
    public void setValidationError(BalmConfigListEditorEntry<T> entry, Component error) {
        context.setValidationError(binding.property(), error);
        updateWidgets();
    }

    @Override
    public void revalidate() {
        if (list != null) {
            final var results = list.children().stream().map(listEntry -> listEntry.validate(binding));
            results.filter(DataResult::isError)
                    .map(DataResult::error)
                    .map(Optional::orElseThrow)
                    .findFirst()
                    .ifPresentOrElse(
                            error -> context.setValidationError(binding.property(), Component.literal(error.message())),
                            () -> context.clearValidationError(binding.property())
                    );
        }

        updateWidgets();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private DataResult<?> validateValues(Collection<T> values) {
        return ((ConfigControlBinding) binding).validateValue(values);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setValues(Collection<T> values) {
        ((ConfigControlBinding) binding).set(values);
    }

    private void updateWidgets() {
        final var validationError = context.getValidationError(binding.property());
        if (validationErrorWidget != null) {
            validationErrorWidget.setMessage(validationError != null ? validationError.copy().withColor(TextColor.DARK_RED) : Component.empty());
            validationErrorWidget.setHeight(validationError != null ? font.lineHeight : 0);
            validationErrorWidget.setMaxWidth(Math.max(1, width - 20));
            validationErrorWidget.setTooltip(validationError != null ? Tooltip.create(validationError) : null);
        }

        if (doneButton != null) {
            doneButton.active = validationError == null;
        }

        repositionElements();
    }

    private int footerHeight() {
        final var validationError = context.getValidationError(binding.property());
        return DEFAULT_FOOTER_HEIGHT + (validationError != null ? font.lineHeight + VALIDATION_ERROR_SPACING : 0);
    }

    public boolean moveValueToTop(BalmConfigListEditorEntry<T> entry) {
        return moveValue(entry, 0);
    }

    public boolean moveValueToBottom(BalmConfigListEditorEntry<T> entry) {
        return moveValue(entry, state.rawValues().size() - 1);
    }

    public boolean moveValue(BalmConfigListEditorEntry<T> entry, int targetIndex) {
        if (!canReorderValues()) {
            return false;
        }

        final var valueEntries = state.values();
        final var valueHolder = entry.valueHolder();
        final int currentIndex = state.indexOf(valueHolder);
        if (currentIndex < 0 || currentIndex >= valueEntries.size()) {
            return false;
        }

        final int clampedTargetIndex = Math.max(0, Math.min(targetIndex, valueEntries.size() - 1));
        if (currentIndex == clampedTargetIndex) {
            return false;
        }

        state.moveValue(valueHolder, clampedTargetIndex);
        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        return getFocused() != null && isDragging() && event.button() == InputConstants.MOUSE_BUTTON_LEFT && getFocused().mouseDragged(event, dragX, dragY);
    }
}
