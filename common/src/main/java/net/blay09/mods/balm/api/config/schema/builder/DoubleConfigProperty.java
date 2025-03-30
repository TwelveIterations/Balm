package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.schema.ConfiguredDouble;
import net.blay09.mods.balm.common.codec.BalmCodecs;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

public class DoubleConfigProperty extends AbstractConfigProperty<Double> implements ConfiguredDouble {
    public static final Codec<Double> CODEC = BalmCodecs.withAlternative(Codec.DOUBLE, Codec.STRING.xmap(Double::parseDouble, String::valueOf));
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
    public StreamCodec<FriendlyByteBuf, Double> streamCodec() {
        return ByteBufCodecs.DOUBLE;
    }

    @Override
    public Double defaultValue() {
        return defaultValue;
    }
}
