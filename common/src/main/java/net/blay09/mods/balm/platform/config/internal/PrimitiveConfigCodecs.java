package net.blay09.mods.balm.platform.config.internal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.NestedTypeHolder;
import net.blay09.mods.balm.platform.config.schema.builder.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.Nullable;

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
        } else if (type == Identifier.class) {
            return (Codec<T>) Identifier.CODEC;
        } else if (type.isEnum() && StringRepresentable.class.isAssignableFrom(type)) {
            return enumCodec((Class) type);
        } else {
            throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
        }
    }

    private static <T extends Enum<T> & StringRepresentable> Codec<T> enumCodec(Class<T> type) {
        return LenientEnumCodecs.fromValues(type::getEnumConstants);
    }

    public static <T> String serializeToString(ConfiguredProperty<T> property, T value) {
        return property.codec().encodeStart(JavaOps.INSTANCE, value)
                .result()
                .map(String::valueOf)
                .orElse(String.valueOf(value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> DataResult<T> parse(ConfiguredProperty<T> property, String value) {
        final var type = property instanceof NestedTypeHolder<?> nestedTypeHolder ? nestedTypeHolder.nestedType() : property.type();
        try {
            return PrimitiveConfigCodecs.codec((Class) type).parse(JavaOps.INSTANCE, value);
        } catch (NumberFormatException e) {
            final var expectedType = expectedNumberType(type);
            if (expectedType != null) {
                return DataResult.error(() -> "Invalid value for " + property.name() + ": expected " + expectedType + ", got \"" + value + "\"");
            }
            return DataResult.error(() -> String.valueOf(e.getMessage()));
        } catch (RuntimeException e) {
            return DataResult.error(() -> String.valueOf(e.getMessage()));
        }
    }

    private static @Nullable String expectedNumberType(Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return "a whole number";
        } else if (type == Long.class || type == long.class) {
            return "a whole number";
        } else if (type == Float.class || type == float.class) {
            return "a decimal number";
        } else if (type == Double.class || type == double.class) {
            return "a decimal number";
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> StreamCodec<ByteBuf, T> streamCodec(Class<T> type) {
        if (type == String.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.STRING_UTF8;
        } else if (type == Integer.class || type == int.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.INT;
        } else if (type == Long.class || type == long.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.LONG;
        } else if (type == Float.class || type == float.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.FLOAT;
        } else if (type == Double.class || type == double.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.DOUBLE;
        } else if (type == Boolean.class || type == boolean.class) {
            return (StreamCodec<ByteBuf, T>) ByteBufCodecs.BOOL;
        } else if (type == Identifier.class) {
            return (StreamCodec<ByteBuf, T>) Identifier.STREAM_CODEC;
        } else if (type.isEnum() && StringRepresentable.class.isAssignableFrom(type)) {
            return enumStreamCodec((Class) type);
        } else {
            throw new IllegalArgumentException("Unsupported nested type: " + type.getName());
        }
    }

    private static <T extends Enum<T> & StringRepresentable> StreamCodec<ByteBuf, T> enumStreamCodec(Class<T> type) {
        final var byIdMapper = ByIdMap.continuous(Enum::ordinal, type.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO);
        return ByteBufCodecs.idMapper(byIdMapper, Enum::ordinal);
    }
}
