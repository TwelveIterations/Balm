package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.schema.ConfiguredLong;
import net.blay09.mods.balm.common.codec.BalmCodecs;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

public class LongConfigProperty extends AbstractConfigProperty<Long> implements ConfiguredLong {
    public static final Codec<Long> CODEC = BalmCodecs.withAlternative(Codec.LONG, Codec.STRING.xmap(Long::parseLong, String::valueOf));
    private final long defaultValue;

    public LongConfigProperty(ConfigPropertyBuilder parent, long defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Long> type() {
        return Long.class;
    }

    @Override
    public Codec<Long> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, Long> streamCodec() {
        return ByteBufCodecs.LONG;
    }

    @Override
    public Long defaultValue() {
        return defaultValue;
    }
}
