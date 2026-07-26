package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.internal.PrimitiveConfigCodecs;
import net.blay09.mods.balm.platform.config.schema.ConfigValidator;
import net.blay09.mods.balm.platform.config.schema.ConfiguredList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListConfigProperty<T> extends AbstractConfigProperty<List<T>> implements ConfiguredList<T> {
    private final Class<T> nestedType;
    private final List<T> defaultValue;
    private final Codec<List<T>> codec;
    private final StreamCodec<ByteBuf, List<T>> streamCodec;
    private final @Nullable ConfigValidator<List<T>> listValidator;

    public ListConfigProperty(ConfigPropertyBuilder parent, Class<T> nestedType, List<T> defaultValue) {
        super(parent);
        this.nestedType = nestedType;
        this.defaultValue = defaultValue;
        this.codec = PrimitiveConfigCodecs.codec(nestedType).listOf();
        this.streamCodec = ByteBufCodecs.collection(ArrayList::new, PrimitiveConfigCodecs.streamCodec(nestedType));
        this.listValidator = createValidator(parent.collectionValidatorClass);
        validateValue(defaultValue).getOrThrow();
    }

    @Override
    public Class<?> type() {
        return List.class;
    }

    @Override
    public Codec<List<T>> codec() {
        return codec.validate(this::validateValue);
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
    public boolean hasCustomCollectionValidator() {
        return listValidator != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataResult<T> validateElement(T value) {
        return validator != null ? ((ConfigValidator<T>) validator).validate(value) : DataResult.success(value);
    }

    @Override
    public DataResult<List<T>> validateValue(List<T> value) {
        for (final var element : value) {
            final var result = validateElement(element);
            if (!result.isSuccess()) {
                return result.map(_ -> value);
            }
        }
        return listValidator != null ? listValidator.validate(value) : DataResult.success(value);
    }

    @Override
    public List<T> defaultValue() {
        return defaultValue;
    }
}
