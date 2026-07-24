package net.blay09.mods.balm.client.platform.config.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.screen.internal.*;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfigControlBinding;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class BalmConfigScreen extends Screen implements BalmConfigScreenContext {

    private static final Component SEARCH_LABEL = Component.translatable("gui.balm.configuration.search");
    private static final Component SEARCH_HINT = SEARCH_LABEL.copy().withStyle(EditBox.SEARCH_HINT_STYLE);

    private final @Nullable Screen parent;
    private final HeaderAndFooterLayout layout;
    private final List<BalmConfigScreenSection> sections;
    private final BalmConfigScreenState state;
    private final Map<BalmConfigScreenRow, BalmConfigScreenRowState> rowStates = new HashMap<>();
    private final Map<ConfiguredProperty<?>, BalmConfigScreenRowState> propertyRowStates = new HashMap<>();
    private final BalmConfigScreenControlFactory controlFactory;

    private @Nullable EditBox searchBox;
    private @Nullable BalmConfigScreenList list;
    private @Nullable Button doneButton;

    public static BalmConfigScreen forMod(@Nullable Screen parent, String modId) {
        return forSchemas(parent, ConfigLocalization.componentForTitle(modId), Balm.config().getSchemasByNamespace(modId));
    }

    public static BalmConfigScreen forSchema(@Nullable Screen parent, BalmConfigSchema schema) {
        return forSchemas(parent, ConfigLocalization.componentForTitle(schema), List.of(schema));
    }

    public static BalmConfigScreen forSchemas(@Nullable Screen parent, Component title, Collection<BalmConfigSchema> schemas) {
        return applySchemas(builder().title(title), schemas).build(parent);
    }

    public BalmConfigScreen(@Nullable Screen parent, Component title, List<BalmConfigScreenSection> sections) {
        super(title);
        this.parent = parent;
        this.sections = sections;
        this.layout = new HeaderAndFooterLayout(this, 36, 33);
        final var parentState = parentState();
        this.state = parentState != null
                ? new BalmConfigScreenState(parentState, this::onValidationChanged, this::onValueChanged)
                : new BalmConfigScreenState(this::onValidationChanged, this::onValueChanged);
        for (final var section : sections) {
            for (final var row : section.rows()) {
                final var rowState = new BalmConfigScreenRowState();
                rowStates.put(row, rowState);
                row.properties().forEach(property -> propertyRowStates.putIfAbsent(property, rowState));
            }
        }
        this.controlFactory = new BalmConfigScreenControlFactory(font, state);
    }

    public static BalmConfigScreenBuilder builder() {
        return new BalmConfigScreenBuilderImpl();
    }

    public static BalmConfigScreenBuilder builder(String modId) {
        return new BalmConfigScreenBuilderImpl().title(ConfigLocalization.componentForTitle(modId));
    }

    @Override
    protected void init() {
        final var header = layout.addToHeader(LinearLayout.vertical().spacing(4));
        header.defaultCellSetting().alignHorizontallyCenter();
        header.addChild(new StringWidget(title, font));
        searchBox = header.addChild(new EditBox(font, 200, 15, SEARCH_LABEL));
        searchBox.setHint(SEARCH_HINT);
        searchBox.setResponder(filter -> {
            refreshList(filter);
            if (list != null) {
                list.setScrollAmount(0);
            }
        });

        list = layout.addToContents(new BalmConfigScreenList(this, controlFactory, sections));

        final var footer = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        doneButton = footer.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> saveAndClose()).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, _ -> onClose()).build());
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
        updateDoneButton();
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }

    @Override
    protected void repositionElements() {
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
        if (event.isEscape() && shouldCloseOnEsc()) {
            if (state.hasValidationErrors()) {
                minecraft.gui.setScreen(new ConfirmScreen(confirmed -> {
                    if (confirmed) {
                        minecraft.gui.setScreen(parent);
                    } else {
                        minecraft.gui.setScreen(this);
                    }
                }, Component.translatable("gui.balm.configuration.discard_changes.title"),
                        Component.translatable("gui.balm.configuration.discard_changes.message"),
                        Component.translatable("gui.balm.configuration.discard_changes.confirm"),
                        CommonComponents.GUI_CANCEL));
            } else {
                saveAndClose();
            }
            return true;
        } else if (event.hasControlDownWithQuirk()
                && event.key() == InputConstants.KEY_F
                && !event.hasShiftDown()
                && !event.hasAltDown()) {
            focusSearchBox();
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

    private void saveAndClose() {
        if (state.hasValidationErrors()) {
            return;
        }

        final var parentState = parentState();
        if (parentState != null) {
            state.applyTo(parentState);
        } else {
            state.save();
        }
        onClose();
    }

    private void updateDoneButton() {
        if (doneButton != null) {
            doneButton.active = !state.hasValidationErrors();
        }
    }

    private void onValidationChanged() {
        updateDoneButton();
    }

    private void onValueChanged() {
        refreshControls();
    }

    private @Nullable BalmConfigScreenState parentState() {
        return parent instanceof BalmConfigScreen parentConfigScreen ? parentConfigScreen.state : null;
    }

    private void refreshList() {
        refreshList(searchBox != null ? searchBox.getValue() : "");
    }

    public void refreshList(String filter) {
        if (list != null) {
            list.populateChildren(filter);
            repositionElements();
        }
    }

    public void refreshControls() {
        refreshList();
        updateDoneButton();
    }

    public int headerHeight() {
        return layout.getHeaderHeight();
    }

    public int contentHeight() {
        return layout.getContentHeight();
    }

    private static BalmConfigScreenBuilder applySchemas(BalmConfigScreenBuilder builder, Collection<BalmConfigSchema> schemas) {
        final var sortedSchemas = schemas.stream()
                .sorted(Comparator.comparing(schema -> schema.identifier().toString()))
                .toList();
        final var includeSchemaHeadings = sortedSchemas.size() > 1;
        for (final var schema : sortedSchemas) {
            final var schemaTitle = Component.translatable(ConfigLocalization.forTitle(schema));
            if (!schema.rootProperties().isEmpty()) {
                builder.section(includeSchemaHeadings ? schemaTitle : Component.empty(), section -> section.properties(schema.rootProperties()));
            }
            for (final var category : schema.categories()) {
                final var categoryTitle = Component.translatable(ConfigLocalization.forCategory(category));
                builder.section(categoryTitle, section -> section.properties(category));
            }
        }
        return builder;
    }

    @Override
    public Font font() {
        return getFont();
    }

    @Override
    public @Nullable Component getValidationError(ConfiguredProperty<?> property) {
        return state.getValidationError(property);
    }

    @Override
    public void setValidationError(ConfiguredProperty<?> property, Component error) {
        state.setValidationError(property, error);
    }

    @Override
    public void clearValidationError(ConfiguredProperty<?> property) {
        state.clearValidationError(property);
    }

    @Override
    public <T> ConfigControlBinding<T> bindingFor(ConfiguredProperty<T> property) {
        return state.bindingFor(property);
    }

    public BalmConfigScreenRowState stateFor(BalmConfigScreenRow row) {
        final var rowState = rowStates.get(row);
        if (rowState == null) {
            throw new IllegalArgumentException("No row state found for configuration row " + row.getClass().getName());
        }
        return rowState;
    }

    @Override
    public BalmConfigScreenRowState stateFor(ConfiguredProperty<?> property) {
        final var rowState = propertyRowStates.get(property);
        if (rowState == null) {
            throw new IllegalArgumentException("No configuration row found for property " + property.name());
        }
        return rowState;
    }
}
