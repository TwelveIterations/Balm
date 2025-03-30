package net.blay09.mods.balm.common.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class ByteBufCodecs {
    public static final StreamCodec<FriendlyByteBuf, Boolean> BOOL = StreamCodec.of(ByteBuf::writeBoolean, ByteBuf::readBoolean);
    public static final StreamCodec<FriendlyByteBuf, Integer> INT = StreamCodec.of(ByteBuf::writeInt, ByteBuf::readInt);
    public static final StreamCodec<FriendlyByteBuf, Double> DOUBLE = StreamCodec.of(ByteBuf::writeDouble, ByteBuf::readDouble);
    public static final StreamCodec<FriendlyByteBuf, Float> FLOAT = StreamCodec.of(ByteBuf::writeFloat, ByteBuf::readFloat);
    public static final StreamCodec<FriendlyByteBuf, Long> LONG = StreamCodec.of(ByteBuf::writeLong, ByteBuf::readLong);
    public static final StreamCodec<FriendlyByteBuf, String> STRING_UTF8 = StreamCodec.of(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION = StreamCodec.of(FriendlyByteBuf::writeResourceLocation,
            FriendlyByteBuf::readResourceLocation);

    public static <T extends Enum<T>> StreamCodec<FriendlyByteBuf, T> idMapper(IntFunction<T> idLookup, ToIntFunction<T> idGetter) {
        return INT.map(idLookup::apply, idGetter::applyAsInt);
    }

    public static <TBuffer extends ByteBuf, TItem, TCollection extends Collection<TItem>> StreamCodec<TBuffer, TCollection> collection(IntFunction<TCollection> factory, StreamCodec<TBuffer, TItem> codec) {
        return new StreamCodec<>() {
            public TCollection decode(TBuffer buffer) {
                final var count = buffer.readInt();
                final var collection = factory.apply(count);
                for (int i = 0; i < count; ++i) {
                    collection.add(codec.decode(buffer));
                }

                return collection;
            }

            public void encode(TBuffer buffer, TCollection collection) {
                buffer.writeInt(collection.size());
                for (final var item : collection) {
                    codec.encode(buffer, item);
                }

            }
        };
    }
}

