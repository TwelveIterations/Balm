package net.blay09.mods.balm.client.platform.config.screen.list.internal;

import org.jspecify.annotations.Nullable;

public class BalmConfigListEditorValue<T> {
    private final @Nullable T originalValue;
    private @Nullable T value;
    private @Nullable Object entryState;

    public BalmConfigListEditorValue(@Nullable T value) {
        this.originalValue = value;
        this.value = value;
    }

    public @Nullable T originalValue() {
        return originalValue;
    }

    public @Nullable T value() {
        return value;
    }

    public void value(@Nullable T value) {
        this.value = value;
    }

    public @Nullable Object entryState() {
        return entryState;
    }

    public void entryState(@Nullable Object entryState) {
        this.entryState = entryState;
    }

    public void reset() {
        value = originalValue;
    }
}
