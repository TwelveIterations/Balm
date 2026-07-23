package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class BalmConfigListEditorState<T> {
    private final List<BalmConfigListEditorValue<T>> values;
    private final Deque<RemovedValue<T>> deletionHistory = new ArrayDeque<>();

    public BalmConfigListEditorState(List<BalmConfigListEditorValue<T>> values) {
        this.values = values;
    }

    public static <T> BalmConfigListEditorState<T> wrap(Collection<T> values) {
        final var wrappedValues = new ArrayList<BalmConfigListEditorValue<T>>();
        for (final T value : values) {
            wrappedValues.add(new BalmConfigListEditorValue<>(value));
        }
        return new BalmConfigListEditorState<>(wrappedValues);
    }

    public List<BalmConfigListEditorValue<T>> values() {
        return values;
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
        if (currentIndex == -1 || currentIndex == targetIndex) {
            return;
        }

        values.remove(currentIndex);
        values.add(targetIndex, value);
    }

    public void removeValue(BalmConfigListEditorValue<T> value) {
        final int index = values.indexOf(value);
        if (index != -1) {
            deletionHistory.addLast(new RemovedValue<>(index, value));
            values.remove(index);
        }
    }

    public boolean restoreDeletedValue() {
        final var removedValue = deletionHistory.pollLast();
        if (removedValue == null) {
            return false;
        }

        final int index = Math.max(0, Math.min(removedValue.index(), values.size()));
        values.add(index, removedValue.value());
        return true;
    }

    private record RemovedValue<T>(int index, BalmConfigListEditorValue<T> value) {
    }

}
