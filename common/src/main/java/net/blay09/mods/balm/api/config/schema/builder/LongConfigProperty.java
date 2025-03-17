package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.schema.ConfiguredLong;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class LongConfigProperty extends AbstractConfigProperty<Long> implements ConfiguredLong {
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
        return Codec.LONG;
    }

    @Override
    public StreamCodec<ByteBuf, Long> streamCodec() {
        return ByteBufCodecs.LONG;
    }

    @Override
    public Long defaultValue() {
        return defaultValue;
    }
}
