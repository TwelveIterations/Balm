package net.blay09.mods.balm.api.config.v2.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredBoolean;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class BooleanConfigProperty extends AbstractConfigProperty<Boolean> implements ConfiguredBoolean {
    private final boolean defaultValue;

    public BooleanConfigProperty(ConfigPropertyBuilder parent, boolean defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Boolean> type() {
        return Boolean.class;
    }

    @Override
    public Codec<Boolean> codec() {
        return Codec.BOOL;
    }

    @Override
    public StreamCodec<ByteBuf, Boolean> streamCodec() {
        return ByteBufCodecs.BOOL;
    }

    @Override
    public Boolean defaultValue() {
        return defaultValue;
    }
}
