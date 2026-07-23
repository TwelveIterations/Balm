package net.blay09.mods.balm.client.platform.config.screen;

import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class BalmConfigScreenRowState {
    private @Nullable Object value;

    public @Nullable Object get() {
        return value;
    }

    public void set(@Nullable Object value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrCreate(Supplier<T> factory) {
        if (value == null) {
            value = factory.get();
        }

        return (T) value;
    }
}
