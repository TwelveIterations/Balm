package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.schema.ConfiguredDouble;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class DoubleConfigProperty extends AbstractConfigProperty<Double> implements ConfiguredDouble {
    private final double defaultValue;

    public DoubleConfigProperty(ConfigPropertyBuilder parent, double defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Double> type() {
        return Double.class;
    }

    @Override
    public Codec<Double> codec() {
        return Codec.DOUBLE;
    }

    @Override
    public StreamCodec<ByteBuf, Double> streamCodec() {
        return ByteBufCodecs.DOUBLE;
    }

    @Override
    public Double defaultValue() {
        return defaultValue;
    }
}
