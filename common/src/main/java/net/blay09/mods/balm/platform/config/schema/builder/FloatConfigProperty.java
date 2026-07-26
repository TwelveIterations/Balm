package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredFloat;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class FloatConfigProperty extends AbstractConfigProperty<Float> implements ConfiguredFloat {
    public static final Codec<Float> CODEC = Codec.withAlternative(Codec.FLOAT, Codec.STRING.xmap(Float::parseFloat, String::valueOf));
    private final float defaultValue;
    private final @Nullable Float minValue;
    private final @Nullable Float maxValue;

    public FloatConfigProperty(ConfigPropertyBuilder parent, float defaultValue) {
        this(parent, defaultValue, null, null);
    }

    public FloatConfigProperty(ConfigPropertyBuilder parent, float defaultValue, float minValue, float maxValue) {
        this(parent, defaultValue, Float.valueOf(minValue), Float.valueOf(maxValue));
    }

    private FloatConfigProperty(ConfigPropertyBuilder parent, float defaultValue, @Nullable Float minValue, @Nullable Float maxValue) {
        super(parent);
        if (minValue != null && maxValue != null && minValue > maxValue) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value for " + name());
        }
        if ((minValue != null && defaultValue < minValue) || (maxValue != null && defaultValue > maxValue)) {
            throw new IllegalArgumentException("Default value out of range for " + name() + ": " + defaultValue);
        }
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        validateValue(defaultValue).getOrThrow();
    }

    @Override
    public Class<Float> type() {
        return Float.class;
    }

    @Override
    public Codec<Float> codec() {
        return CODEC.validate(this::validateValue);
    }

    @Override
    public StreamCodec<ByteBuf, Float> streamCodec() {
        return ByteBufCodecs.FLOAT;
    }

    @Override
    public Float defaultValue() {
        return defaultValue;
    }

    @Override
    public Optional<Float> minValue() {
        return Optional.ofNullable(minValue);
    }

    @Override
    public Optional<Float> maxValue() {
        return Optional.ofNullable(maxValue);
    }

    @Override
    public DataResult<Float> validateValue(Float value) {
        if (minValue != null && value < minValue) {
            return DataResult.error(() -> "Value for " + name() + " is below minimum " + minValue + ": " + value);
        }
        if (maxValue != null && value > maxValue) {
            return DataResult.error(() -> "Value for " + name() + " is above maximum " + maxValue + ": " + value);
        }
        return super.validateValue(value);
    }
}
