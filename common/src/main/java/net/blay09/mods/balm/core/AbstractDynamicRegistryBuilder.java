package net.blay09.mods.balm.core;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDynamicRegistryBuilder<T> implements DynamicRegistryBuilder<T> {
    private boolean sync;
    private boolean skipSyncWhenEmpty;
    private @Nullable Codec<T> networkCodec;

    @Override
    public DynamicRegistryBuilder<T> sync() {
        this.sync = true;
        return this;
    }

    @Override
    public DynamicRegistryBuilder<T> sync(Codec<T> networkCodec) {
        this.sync = true;
        this.networkCodec = networkCodec;
        return this;
    }

    @Override
    public DynamicRegistryBuilder<T> skipSyncWhenEmpty() {
        this.skipSyncWhenEmpty = true;
        return this;
    }

    public boolean shouldSync() {
        return sync;
    }

    public boolean shouldSkipSyncWhenEmpty() {
        return skipSyncWhenEmpty;
    }

    public @Nullable Codec<T> getNetworkCodec() {
        return networkCodec;
    }
}
