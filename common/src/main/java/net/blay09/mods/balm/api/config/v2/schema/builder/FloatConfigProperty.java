package net.blay09.mods.balm.api.config.v2.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredFloat;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class FloatConfigProperty extends AbstractConfigProperty<Float> implements ConfiguredFloat {
    private final float defaultValue;

    public FloatConfigProperty(ConfigPropertyBuilder parent, float defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Float> type() {
        return Float.class;
    }

    @Override
    public Codec<Float> codec() {
        return Codec.FLOAT;
    }

    @Override
    public StreamCodec<ByteBuf, Float> streamCodec() {
        return ByteBufCodecs.FLOAT;
    }

    @Override
    public Float defaultValue() {
        return defaultValue;
    }
}
