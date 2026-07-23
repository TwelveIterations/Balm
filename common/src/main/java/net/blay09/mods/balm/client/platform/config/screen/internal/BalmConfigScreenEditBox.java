package net.blay09.mods.balm.client.platform.config.screen.internal;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.util.ConfigLocalization;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class BalmConfigScreenEditBox<T> extends EditBox {

    public BalmConfigScreenEditBox(Font font, ConfiguredProperty<T> property, BalmConfigScreenState screenState, BalmConfigScreenRowState rowState) {
        super(font, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.translatable(ConfigLocalization.forProperty(property)));
        setMaxLength(2048);

        final var textValue = serialize(property, property.getRaw(screenState.configFor(property)));
        setValue(rowState.get() instanceof String storedTextValue ? storedTextValue : textValue);
        setResponder(value -> {
            rowState.set(value);
            final var result = parse(property, value);
            result.error().ifPresentOrElse(error -> screenState.setValidationError(property, Component.literal(error.message())),
                    () -> result.result().ifPresent(parsedValue -> screenState.trySetValue(property, parsedValue)));
        });
    }

    private static <T> String serialize(ConfiguredProperty<T> property, T value) {
        return property.codec().encodeStart(JavaOps.INSTANCE, value)
                .result()
                .map(String::valueOf)
                .orElse(String.valueOf(value));
    }

    private static <T> DataResult<T> parse(ConfiguredProperty<T> property, String value) {
        try {
            return property.codec().parse(JavaOps.INSTANCE, value);
        } catch (NumberFormatException e) {
            final var expectedType = expectedNumberType(property.type());
            if (expectedType != null) {
                return DataResult.error(() -> "Invalid value for " + property.name() + ": expected " + expectedType + ", got \"" + value + "\"");
            }
            return DataResult.error(() -> String.valueOf(e.getMessage()));
        } catch (RuntimeException e) {
            return DataResult.error(() -> String.valueOf(e.getMessage()));
        }
    }

    private static @Nullable String expectedNumberType(Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return "a whole number";
        } else if (type == Long.class || type == long.class) {
            return "a whole number";
        } else if (type == Float.class || type == float.class) {
            return "a decimal number";
        } else if (type == Double.class || type == double.class) {
            return "a decimal number";
        }
        return null;
    }
}
