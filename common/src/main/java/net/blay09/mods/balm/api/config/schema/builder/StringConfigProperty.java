package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.schema.ConfiguredString;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

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
    public StreamCodec<FriendlyByteBuf, String> streamCodec() {
        return ByteBufCodecs.STRING_UTF8;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }
}
