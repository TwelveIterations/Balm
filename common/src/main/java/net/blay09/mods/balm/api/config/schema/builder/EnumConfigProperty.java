package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.LenientEnumCodecs;
import net.blay09.mods.balm.api.config.schema.ConfiguredEnum;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ByIdMap;

public class EnumConfigProperty<T extends Enum<T>> extends AbstractConfigProperty<T> implements ConfiguredEnum<T> {
    private final T defaultValue;
    private final Codec<T> codec;
    private final StreamCodec<FriendlyByteBuf, T> streamCodec;

    public EnumConfigProperty(ConfigPropertyBuilder parent, T defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
        final var enumClass = defaultValue.getDeclaringClass();
        final var byIdMapper = ByIdMap.continuous(Enum::ordinal, enumClass.getEnumConstants(), ByIdMap.OutOfBoundsStrategy.ZERO);
        this.codec = LenientEnumCodecs.fromValues(enumClass::getEnumConstants);
        this.streamCodec = ByteBufCodecs.idMapper(byIdMapper, Enum::ordinal);
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
    public StreamCodec<FriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    @Override
    public T defaultValue() {
        return defaultValue;
    }
}
