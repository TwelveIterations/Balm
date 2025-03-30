package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.schema.ConfiguredInt;
import net.blay09.mods.balm.common.codec.BalmCodecs;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

public class IntConfigProperty extends AbstractConfigProperty<Integer> implements ConfiguredInt {
    public static final Codec<Integer> CODEC = BalmCodecs.withAlternative(Codec.INT, Codec.STRING.xmap(Integer::parseInt, String::valueOf));
    private final int defaultValue;

    public IntConfigProperty(ConfigPropertyBuilder parent, int defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }

    @Override
    public Codec<Integer> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, Integer> streamCodec() {
        return ByteBufCodecs.INT;
    }

    @Override
    public Integer defaultValue() {
        return defaultValue;
    }
}
