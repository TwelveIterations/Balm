package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.ConfigCodecs;
import net.blay09.mods.balm.api.config.schema.ConfiguredList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public class ListConfigProperty<T> extends AbstractConfigProperty<List<T>> implements ConfiguredList<T> {
    private final Class<T> nestedType;
    private final List<T> defaultValue;
    private final Codec<List<T>> codec;
    private final StreamCodec<ByteBuf, List<T>> streamCodec;

    public ListConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, List<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
        this.codec = ConfigCodecs.codec(nestedType).listOf();
        this.streamCodec = ByteBufCodecs.collection(ArrayList::new, ConfigCodecs.streamCodec(nestedType));
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
    public StreamCodec<ByteBuf, List<T>> streamCodec() {
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
