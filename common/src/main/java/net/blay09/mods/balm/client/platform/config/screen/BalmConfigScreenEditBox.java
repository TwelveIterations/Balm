package net.blay09.mods.balm.client.platform.config.screen;

import net.blay09.mods.balm.client.platform.config.screen.internal.BalmConfigScreenState;
import net.blay09.mods.balm.platform.config.internal.PrimitiveConfigCodecs;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class BalmConfigScreenEditBox<T> extends EditBox {

    public BalmConfigScreenEditBox(Font font, ConfiguredProperty<T> property, BalmConfigScreenState screenState, BalmConfigScreenRowState rowState) {
        super(font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.translatable(ConfigLocalization.forProperty(property)));
        setMaxLength(2048);

        final var textValue = PrimitiveConfigCodecs.serializeToString(property, property.getRaw(screenState.configFor(property)));
        setValue(rowState.get() instanceof String storedTextValue ? storedTextValue : textValue);
        setResponder(value -> {
            rowState.set(value);
            final var result = PrimitiveConfigCodecs.parse(property, value);
            result.error().ifPresentOrElse(error -> screenState.setValidationError(property, Component.literal(error.message())),
                    () -> result.result().ifPresent(parsedValue -> screenState.trySetValue(property, parsedValue)));
        });
    }

}
