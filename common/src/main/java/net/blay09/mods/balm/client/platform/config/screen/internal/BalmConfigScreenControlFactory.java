package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.internal.ConfigControlContextImpl;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlRegistry;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreen;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenEditBox;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
import net.blay09.mods.balm.client.platform.config.screen.list.BalmConfigListEditorScreen;
import net.blay09.mods.balm.client.platform.config.screen.list.internal.BalmConfigListEditorState;
import net.blay09.mods.balm.platform.config.schema.ConfiguredEnum;
import net.blay09.mods.balm.platform.config.schema.ConfiguredList;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.ConfiguredSet;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.List;

public class BalmConfigScreenControlFactory {
    private final Font font;
    private final BalmConfigScreenState state;

    public BalmConfigScreenControlFactory(Font font, BalmConfigScreenState state) {
        this.font = font;
        this.state = state;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractWidget createControl(BalmConfigScreen screen, ConfiguredProperty<?> property, BalmConfigScreenRowState rowState) {
        final var label = Component.translatable(ConfigLocalization.forProperty(property));
        final var customControlId = property.customControl().orElse(null);
        if (customControlId != null) {
            final var binding = state.bindingFor((ConfiguredProperty) property);
            final var context = new ConfigControlContextImpl(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, state::bindingFor);
            final var element = ConfigControlRegistry.createElement(customControlId, binding, context).orElse(null);
            if (element instanceof AbstractWidget widget) {
                widget.setWidth(Button.DEFAULT_WIDTH);
                widget.setHeight(Button.DEFAULT_HEIGHT);
                return widget;
            }
        }

        if (property.type() == Boolean.class) {
            final var booleanProperty = (ConfiguredProperty<Boolean>) property;
            return CycleButton.onOffBuilder(booleanProperty.getRaw(state.configFor(booleanProperty)))
                    .displayOnlyValue()
                    .create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, label,
                            (_, value) -> state.trySetValue(booleanProperty, value));
        } else if (property instanceof ConfiguredEnum<?> enumProperty) {
            return createEnumControl((ConfiguredEnum) enumProperty, label);
        } else if (property instanceof ConfiguredList<?> listProperty) {
            final var values = resolveCollectionValue(listProperty, rowState);
            return Button.builder(Component.translatable("gui.balm.configuration.list.items", values.size()),
                            _ -> Minecraft.getInstance().gui.setScreen(BalmConfigListEditorScreen.builder(screen, screen, state.bindingFor(listProperty))
                                    .build()))
                    .width(Button.DEFAULT_WIDTH)
                    .build();
        } else if (property instanceof ConfiguredSet<?> setProperty) {
            final var values = resolveCollectionValue(setProperty, rowState);
            return Button.builder(Component.translatable("gui.balm.configuration.list.items", values.size()),
                            _ -> Minecraft.getInstance().gui.setScreen(BalmConfigListEditorScreen.builder(screen, screen, state.bindingFor(setProperty))
                                    .build()))
                    .width(Button.DEFAULT_WIDTH)
                    .build();
        }

        return new BalmConfigScreenEditBox<>(font, (ConfiguredProperty) property, state, rowState);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractWidget createMergedPropertiesControl(BalmConfigScreenMergedPropertiesRow row, BalmConfigScreenRowState rowState) {
        final var context = new ConfigControlContextImpl(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, property -> state.bindingFor((ConfiguredProperty) property));
        final var widget = row.widgetFactory().apply(context, rowState);
        widget.setWidth(Button.DEFAULT_WIDTH);
        widget.setHeight(Button.DEFAULT_HEIGHT);
        return widget;
    }

    private <T extends Enum<T>> AbstractWidget createEnumControl(ConfiguredEnum<T> property, Component label) {
        return CycleButton.builder(value -> ConfigLocalization.forEnumValue(property, value), property.getRaw(state.configFor(property)))
                .withValues(property.validValues())
                .displayOnlyValue()
                .create(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, label,
                        (_, value) -> state.trySetValue(property, value));
    }

    private Collection<?> resolveCollectionValue(ConfiguredProperty<?> property, BalmConfigScreenRowState rowState) {
        final var binding = state.bindingFor(property);
        if (rowState.get() instanceof BalmConfigListEditorState<?> editorState) {
            return editorState.rawValues();
        }

        if (binding.get() instanceof Collection<?> collection) {
            return collection;
        }

        return List.of();
    }

}
