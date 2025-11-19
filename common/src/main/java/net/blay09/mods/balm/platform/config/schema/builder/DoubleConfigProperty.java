package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredDouble;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class DoubleConfigProperty extends AbstractConfigProperty<Double> implements ConfiguredDouble {
    public static final Codec<Double> CODEC = Codec.withAlternative(Codec.DOUBLE, Codec.STRING.xmap(Double::parseDouble, String::valueOf));
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
        return CODEC;
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
