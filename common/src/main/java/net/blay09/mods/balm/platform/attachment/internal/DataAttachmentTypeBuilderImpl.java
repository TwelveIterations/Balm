package net.blay09.mods.balm.platform.attachment.internal;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.platform.attachment.DataAttachmentTypeBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class DataAttachmentTypeBuilderImpl<T> implements DataAttachmentTypeBuilder<T> {
    @Nullable
    private Supplier<T> initializer;
    @Nullable
    private Codec<T> persistentCodec;
    @Nullable
    private StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
    @Nullable
    private BiPredicate<Object, ServerPlayer> syncPredicate;
    private boolean copyOnDeath;

    @Override
    public DataAttachmentTypeBuilder<T> initializer(Supplier<T> initializer) {
        this.initializer = initializer;
        return this;
    }

    @Override
    public DataAttachmentTypeBuilder<T> persistent(Codec<T> codec) {
        this.persistentCodec = codec;
        return this;
    }

    @Override
    public DataAttachmentTypeBuilder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, BiPredicate<Object, ServerPlayer> predicate) {
        this.streamCodec = streamCodec;
        this.syncPredicate = predicate;
        return this;
    }

    @Override
    public DataAttachmentTypeBuilder<T> copyOnDeath() {
        this.copyOnDeath = true;
        return this;
    }

    public @Nullable Supplier<T> getInitializer() {
        return initializer;
    }

    public @Nullable Codec<T> getPersistentCodec() {
        return persistentCodec;
    }

    public @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> getStreamCodec() {
        return streamCodec;
    }

    public @Nullable BiPredicate<Object, ServerPlayer> getSyncPredicate() {
        return syncPredicate;
    }

    public boolean isCopyOnDeath() {
        return copyOnDeath;
    }
}
