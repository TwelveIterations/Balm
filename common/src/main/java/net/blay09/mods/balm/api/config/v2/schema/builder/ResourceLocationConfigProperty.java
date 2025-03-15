package net.blay09.mods.balm.api.config.v2.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredResourceLocation;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationConfigProperty extends AbstractConfigProperty<ResourceLocation> implements ConfiguredResourceLocation {
    private final ResourceLocation defaultValue;

    public ResourceLocationConfigProperty(ConfigPropertyBuilder parent, ResourceLocation defaultValue) {
        super(parent);
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<ResourceLocation> type() {
        return ResourceLocation.class;
    }

    @Override
    public Codec<ResourceLocation> codec() {
        return ResourceLocation.CODEC;
    }

    @Override
    public StreamCodec<ByteBuf, ResourceLocation> streamCodec() {
        return ResourceLocation.STREAM_CODEC;
    }

    @Override
    public ResourceLocation defaultValue() {
        return defaultValue;
    }
}
