package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.schema.ConfiguredString;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class StringConfigProperty extends AbstractConfigProperty<String> implements ConfiguredString {
    private final String defaultValue;

    public StringConfigProperty(ConfigPropertyBuilder parent, String defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<String> type() {
        return String.class;
    }

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }

    @Override
    public StreamCodec<ByteBuf, String> streamCodec() {
        return ByteBufCodecs.STRING_UTF8;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }
}
