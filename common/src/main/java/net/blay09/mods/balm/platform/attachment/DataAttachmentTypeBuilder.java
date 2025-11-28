package net.blay09.mods.balm.platform.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public interface DataAttachmentTypeBuilder<T> {
    DataAttachmentTypeBuilder<T> initializer(Supplier<T> initializer);

    DataAttachmentTypeBuilder<T> persistent(Codec<T> codec);

    default DataAttachmentTypeBuilder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return networkSynchronized(streamCodec, (ignored, player) -> true);
    }

    DataAttachmentTypeBuilder<T> networkSynchronized(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, BiPredicate<Object, ServerPlayer> predicate);

    DataAttachmentTypeBuilder<T> copyOnDeath();
}
