package net.blay09.mods.balm.core;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class AbstractCustomRegistryBuilder<T> implements CustomRegistryBuilder<T> {
    private @Nullable Identifier defaultKey;
    private boolean sync;

    @Override
    public CustomRegistryBuilder<T> defaultKey(Identifier defaultKey) {
        this.defaultKey = defaultKey;
        return this;
    }

    @Override
    public CustomRegistryBuilder<T> sync() {
        this.sync = true;
        return this;
    }

    public @Nullable Identifier getDefaultKey() {
        return defaultKey;
    }

    public boolean shouldSync() {
        return sync;
    }
}
