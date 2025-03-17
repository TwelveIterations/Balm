package net.blay09.mods.balm.api.config;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ConfigCodecs {
    public static <T> Codec<T> codec(Class<T> type) {
        if (type == String.class) {
            return (Codec<T>) Codec.STRING;
        } else {
            throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
        }
    }

    public static <T> StreamCodec<ByteBuf, T> streamCodec(Class<T> type) {
        if (type == String.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.STRING_UTF8;
        } else {
            throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
        }
    }
}
