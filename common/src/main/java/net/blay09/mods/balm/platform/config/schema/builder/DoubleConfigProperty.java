package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredDouble;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class DoubleConfigProperty extends AbstractConfigProperty<Double> implements ConfiguredDouble {
    public static final Codec<Double> CODEC = Codec.withAlternative(Codec.DOUBLE, Codec.STRING.xmap(Double::parseDouble, String::valueOf));
    private final double defaultValue;
    private final @Nullable Double minValue;
    private final @Nullable Double maxValue;

    public DoubleConfigProperty(ConfigPropertyBuilder parent, double defaultValue) {
        this(parent, defaultValue, null, null);
    }

    public DoubleConfigProperty(ConfigPropertyBuilder parent, double defaultValue, double minValue, double maxValue) {
        this(parent, defaultValue, Double.valueOf(minValue), Double.valueOf(maxValue));
    }

    private DoubleConfigProperty(ConfigPropertyBuilder parent, double defaultValue, @Nullable Double minValue, @Nullable Double maxValue) {
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
    public Class<Double> type() {
        return Double.class;
    }

    @Override
    public Codec<Double> codec() {
        return CODEC.validate(this::validateValue);
    }

    @Override
    public StreamCodec<ByteBuf, Double> streamCodec() {
        return ByteBufCodecs.DOUBLE;
    }

    @Override
    public Double defaultValue() {
        return defaultValue;
    }

    @Override
    public Optional<Double> minValue() {
        return Optional.ofNullable(minValue);
    }

    @Override
    public Optional<Double> maxValue() {
        return Optional.ofNullable(maxValue);
    }

    @Override
    public DataResult<Double> validateValue(Double value) {
        if (minValue != null && value < minValue) {
            return DataResult.error(() -> "Value for " + name() + " is below minimum " + minValue + ": " + value);
        }
        if (maxValue != null && value > maxValue) {
            return DataResult.error(() -> "Value for " + name() + " is above maximum " + maxValue + ": " + value);
        }
        return super.validateValue(value);
    }
}
