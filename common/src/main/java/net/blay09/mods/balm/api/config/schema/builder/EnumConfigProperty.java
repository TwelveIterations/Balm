package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.schema.ConfiguredEnum;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public class EnumConfigProperty<T extends Enum<T> & StringRepresentable> extends AbstractConfigProperty<T> implements ConfiguredEnum<T> {
    private final T defaultValue;
    private final Codec<T> codec;
    private final StreamCodec<ByteBuf, T> streamCodec;

    public EnumConfigProperty(ConfigPropertyBuilder parent, T defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
        final var enumClass = defaultValue.getDeclaringClass();
        final var byIdMapper = ByIdMap.continuous(Enum::ordinal, enumClass.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO);
        this.codec = StringRepresentable.fromEnum(enumClass::getEnumConstants);
        this.streamCodec = ByteBufCodecs.idMapper(byIdMapper, Enum::ordinal).cast();
    }

    @Override
    public Class<T> type() {
        return defaultValue.getDeclaringClass();
    }

    @Override
    public Codec<T> codec() {
        return codec;
    }

    @Override
    public StreamCodec<ByteBuf, T> streamCodec() {
        return streamCodec;
    }

    @Override
    public T defaultValue() {
        return defaultValue;
    }
}
