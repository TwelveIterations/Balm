package net.blay09.mods.balm.client.platform.config.screen.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.blay09.mods.balm.client.platform.config.screen.BalmConfigScreenRowState;
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
        return property.codec().encodeStart(JsonOps.INSTANCE, value)
                .result()
                .map(json -> json.isJsonPrimitive() && json.getAsJsonPrimitive().isString() ? json.getAsString() : json.toString())
                .orElse(String.valueOf(value));
    }

    private static <T> DataResult<T> parse(ConfiguredProperty<T> property, String value) {
        try {
            final JsonElement json = value.startsWith("[") || value.startsWith("{") || value.startsWith("\"") ? JsonParser.parseString(value) : new JsonPrimitive(value);
            return property.codec().parse(JsonOps.INSTANCE, json);
        } catch (RuntimeException e) {
            return DataResult.error(() -> String.valueOf(e.getMessage()));
        }
    }
}
