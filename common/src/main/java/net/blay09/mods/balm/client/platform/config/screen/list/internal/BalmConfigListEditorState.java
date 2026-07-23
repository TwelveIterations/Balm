package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record BalmConfigListEditorState<T>(List<BalmConfigListEditorValue<T>> values) {
    public static <T> BalmConfigListEditorState<T> wrap(Collection<T> values) {
        final var wrappedValues = new ArrayList<BalmConfigListEditorValue<T>>();
        for (final T value : values) {
            wrappedValues.add(new BalmConfigListEditorValue<>(value));
        }
        return new BalmConfigListEditorState<>(wrappedValues);
    }

    public Collection<T> rawValues() {
        final var rawValues = new ArrayList<T>();
        for (final var valueHolder : values) {
            final var value = valueHolder.value();
            if (value != null) {
                rawValues.add(value);
            }
        }
        return rawValues;
    }

    public int indexOf(BalmConfigListEditorValue<T> value) {
        return values.indexOf(value);
    }

    public BalmConfigListEditorValue<T> addValue() {
        final var editorValue = new BalmConfigListEditorValue<T>(null);
        values.add(editorValue);
        return editorValue;
    }

    public void moveValue(BalmConfigListEditorValue<T> value, int targetIndex) {
        final int currentIndex = values.indexOf(value);
        if (currentIndex == -1) {
            return;
        }

        values.remove(currentIndex);
        values.add(targetIndex, value);
    }

    public void removeValue(BalmConfigListEditorValue<T> value) {
        values.remove(value);
    }

}
