package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.PrimitiveConfigCodecs;
import net.blay09.mods.balm.api.config.schema.ConfiguredList;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class ListConfigProperty<T> extends AbstractConfigProperty<List<T>> implements ConfiguredList<T> {
    private final Class<T> nestedType;
    private final List<T> defaultValue;
    private final Codec<List<T>> codec;
    private final StreamCodec<FriendlyByteBuf, List<T>> streamCodec;

    public ListConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, List<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
        this.codec = PrimitiveConfigCodecs.codec(nestedType).listOf();
        this.streamCodec = ByteBufCodecs.collection(ArrayList::new, PrimitiveConfigCodecs.streamCodec(nestedType));
    }

    @Override
    public Class<?> type() {
        return List.class;
    }

    @Override
    public Codec<List<T>> codec() {
        return codec;
    }

    @Override
    public StreamCodec<FriendlyByteBuf, List<T>> streamCodec() {
        return streamCodec;
    }

    @Override
    public Class<T> nestedType() {
        return nestedType;
    }

    @Override
    public List<T> defaultValue() {
        return defaultValue;
    }
}
