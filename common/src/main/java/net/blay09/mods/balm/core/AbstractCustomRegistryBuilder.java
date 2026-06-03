package net.blay09.mods.balm.core;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCustomRegistryBuilder<T> implements CustomRegistryBuilder<T> {
    private @Nullable ResourceLocation defaultKey;
    private boolean sync;

    @Override
    public CustomRegistryBuilder<T> defaultKey(ResourceLocation defaultKey) {
        this.defaultKey = defaultKey;
        return this;
    }

    @Override
    public CustomRegistryBuilder<T> sync() {
        this.sync = true;
        return this;
    }

    public @Nullable ResourceLocation getDefaultKey() {
        return defaultKey;
    }

    public boolean shouldSync() {
        return sync;
    }
}
