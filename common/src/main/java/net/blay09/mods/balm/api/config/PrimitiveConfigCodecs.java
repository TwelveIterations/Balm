package net.blay09.mods.balm.api.config;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.schema.builder.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;

public class PrimitiveConfigCodecs {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Codec<T> codec(Class<T> type) {
        if (type == String.class) {
            return (Codec<T>) Codec.STRING;
        } else if (type == Integer.class || type == int.class) {
            return (Codec<T>) IntConfigProperty.CODEC;
        } else if (type == Long.class || type == long.class) {
            return (Codec<T>) LongConfigProperty.CODEC;
        } else if (type == Float.class || type == float.class) {
            return (Codec<T>) FloatConfigProperty.CODEC;
        } else if (type == Double.class || type == double.class) {
            return (Codec<T>) DoubleConfigProperty.CODEC;
        } else if (type == Boolean.class || type == boolean.class) {
            return (Codec<T>) BooleanConfigProperty.CODEC;
        } else if (type == ResourceLocation.class) {
            return (Codec<T>) ResourceLocation.CODEC;
        } else if (type.isEnum()) {
            return enumCodec((Class) type);
        } else {
            throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
        }
    }

    private static <T extends Enum<T>> Codec<T> enumCodec(Class<T> type) {
        return LenientEnumCodecs.fromValues(type::getEnumConstants);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> StreamCodec<ByteBuf, T> streamCodec(Class<T> type) {
        if (type == String.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.STRING_UTF8;
        } else if (type == Integer.class || type == int.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.INT;
        } else if (type == Long.class || type == long.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.VAR_LONG;
        } else if (type == Float.class || type == float.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.FLOAT;
        } else if (type == Double.class || type == double.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.DOUBLE;
        } else if (type == Boolean.class || type == boolean.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.BOOL;
        } else if (type == ResourceLocation.class) {
            return (StreamCodec<ByteBuf, T>) ResourceLocation.STREAM_CODEC;
        } else if (type.isEnum()) {
            return enumStreamCodec((Class) type);
        } else {
            throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
        }
    }

    private static <T extends Enum<T>> StreamCodec<ByteBuf, T> enumStreamCodec(Class<T> type) {
        final var byIdMapper = ByIdMap.continuous(Enum::ordinal, type.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO);
        return ByteBufCodecs.idMapper(byIdMapper, Enum::ordinal);
    }
}
