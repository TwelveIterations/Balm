package net.blay09.mods.balm.api.config.schema.builder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.config.PrimitiveConfigCodecs;
import net.blay09.mods.balm.api.config.schema.ConfiguredSet;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SetConfigProperty<T> extends AbstractConfigProperty<Set<T>> implements ConfiguredSet<T> {
    private final Class<T> nestedType;
    private final Set<T> defaultValue;
    private final Codec<List<T>> codec;
    private final StreamCodec<ByteBuf, List<T>> streamCodec;

    public SetConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, Set<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
        this.codec = PrimitiveConfigCodecs.codec(nestedType).listOf();
        this.streamCodec = ByteBufCodecs.collection(ArrayList::new, PrimitiveConfigCodecs.streamCodec(nestedType));
    }

    @Override
    public Class<?> type() {
        return Set.class;
    }

    @Override
    public Codec<Set<T>> codec() {
        return codec.xmap(Set::copyOf, List::copyOf);
    }

    @Override
    public StreamCodec<ByteBuf, Set<T>> streamCodec() {
        return streamCodec.map(Set::copyOf, List::copyOf);
    }

    @Override
    public Class<T> nestedType() {
        return nestedType;
    }

    @Override
    public Set<T> defaultValue() {
        return defaultValue;
    }
}
