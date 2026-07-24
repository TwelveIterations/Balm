package net.blay09.mods.balm.client.platform.config.screen;

import net.blay09.mods.balm.platform.config.internal.PrimitiveConfigCodecs;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class BalmConfigScreenEditBox<T> extends EditBox {

    public BalmConfigScreenEditBox(Font font, ConfiguredProperty<T> property, BalmConfigScreenContext context, BalmConfigScreenRowState rowState) {
        super(font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.translatable(ConfigLocalization.forProperty(property)));
        setMaxLength(2048);

        final var binding = context.bindingFor(property);
        final var textValue = PrimitiveConfigCodecs.serializeToString(property, binding.get());
        setValue(rowState.get() instanceof String storedTextValue ? storedTextValue : textValue);
        setResponder(value -> {
            rowState.set(value);
            final var result = PrimitiveConfigCodecs.parse(property, value);
            result.error().ifPresentOrElse(error -> context.setValidationError(property, Component.literal(error.message())),
                    () -> result.result().ifPresent(parsedValue -> {
                        final var validationResult = binding.validateValue(parsedValue);
                        validationResult.error().ifPresentOrElse(error -> context.setValidationError(property, Component.literal(error.message())),
                                () -> {
                                    binding.setter().accept(validationResult.getOrThrow());
                                    context.clearValidationError(property);
                                });
                    }));
        });
    }

}
