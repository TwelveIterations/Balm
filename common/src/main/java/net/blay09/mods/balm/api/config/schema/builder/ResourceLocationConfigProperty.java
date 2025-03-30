package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.schema.ConfiguredResourceLocation;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
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
    public StreamCodec<FriendlyByteBuf, ResourceLocation> streamCodec() {
        return ByteBufCodecs.RESOURCE_LOCATION;
    }

    @Override
    public ResourceLocation defaultValue() {
        return defaultValue;
    }
}
