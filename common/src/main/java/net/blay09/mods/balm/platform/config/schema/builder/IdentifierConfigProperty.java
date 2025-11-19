package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredIdentifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class IdentifierConfigProperty extends AbstractConfigProperty<Identifier> implements ConfiguredIdentifier {
    private final Identifier defaultValue;

    public IdentifierConfigProperty(ConfigPropertyBuilder parent, Identifier defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<Identifier> type() {
        return Identifier.class;
    }

    @Override
    public Codec<Identifier> codec() {
        return Identifier.CODEC;
    }

    @Override
    public StreamCodec<ByteBuf, Identifier> streamCodec() {
        return Identifier.STREAM_CODEC;
    }

    @Override
    public Identifier defaultValue() {
        return defaultValue;
    }
}
