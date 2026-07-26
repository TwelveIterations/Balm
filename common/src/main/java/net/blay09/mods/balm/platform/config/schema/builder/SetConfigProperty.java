package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.internal.PrimitiveConfigCodecs;
import net.blay09.mods.balm.platform.config.schema.ConfigValidator;
import net.blay09.mods.balm.platform.config.schema.ConfiguredSet;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SetConfigProperty<T> extends AbstractConfigProperty<Set<T>> implements ConfiguredSet<T> {
    private final Class<T> nestedType;
    private final Set<T> defaultValue;
    private final Codec<List<T>> codec;
    private final StreamCodec<ByteBuf, List<T>> streamCodec;
    private final @Nullable ConfigValidator<Set<T>> setValidator;

    public SetConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, Set<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
        this.codec = PrimitiveConfigCodecs.codec(nestedType).listOf();
        this.streamCodec = ByteBufCodecs.collection(ArrayList::new, PrimitiveConfigCodecs.streamCodec(nestedType));
        this.setValidator = createValidator(parent.collectionValidatorClass);
        validateValue(defaultValue).getOrThrow();
    }

    @Override
    public Class<?> type() {
        return Set.class;
    }

    @Override
    public Codec<Set<T>> codec() {
        return codec.xmap(Set::copyOf, List::copyOf).validate(this::validateValue);
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
    public boolean hasCustomCollectionValidator() {
        return setValidator != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<T> validateElement(T value) {
        return validator != null ? ((ConfigValidator<T>) validator).validate(value) : DataResult.success(value);
    }

    @Override
    public DataResult<Set<T>> validateValue(Set<T> value) {
        for (final var element : value) {
            final var result = validateElement(element);
            if (!result.isSuccess()) {
                return result.map(_ -> value);
            }
        }
        return setValidator != null ? setValidator.validate(value) : DataResult.success(value);
    }

    @Override
    public Set<T> defaultValue() {
        return defaultValue;
    }
}
