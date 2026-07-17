package net.blay09.mods.balm.client.platform.config.screen.internal;

import net.blay09.mods.balm.client.platform.config.internal.ConfigControlContextImpl;
import net.blay09.mods.balm.client.platform.config.internal.ConfigControlRegistry;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
import net.blay09.mods.balm.platform.config.schema.ConfiguredEnum;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;

public class BalmConfigScreenControlFactory {
    private final Font font;
    private final BalmConfigScreenState state;

    public BalmConfigScreenControlFactory(Font font, BalmConfigScreenState state) {
        this.font = font;
        this.state = state;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractWidget createControl(ConfiguredProperty<?> property, BalmConfigScreenRowState controlState) {
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
        }

        return new BalmConfigScreenEditBox<>(font, (ConfiguredProperty) property, state, controlState);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractWidget createMergedPropertiesControl(BalmConfigScreenMergedPropertiesRow row) {
        final var context = new ConfigControlContextImpl(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, property -> state.bindingFor((ConfiguredProperty) property));
        final var widget = row.widgetFactory().apply(context, row.state());
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

}
