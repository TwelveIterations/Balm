package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredInt;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class IntConfigProperty extends AbstractConfigProperty<Integer> implements ConfiguredInt {
    public static final Codec<Integer> CODEC = Codec.withAlternative(Codec.INT, Codec.STRING.xmap(Integer::parseInt, String::valueOf));
    private final int defaultValue;
    private final @Nullable Integer minValue;
    private final @Nullable Integer maxValue;

    public IntConfigProperty(ConfigPropertyBuilder parent, int defaultValue) {
        this(parent, defaultValue, null, null);
    }

    public IntConfigProperty(ConfigPropertyBuilder parent, int defaultValue, int minValue, int maxValue) {
        this(parent, defaultValue, Integer.valueOf(minValue), Integer.valueOf(maxValue));
    }

    private IntConfigProperty(ConfigPropertyBuilder parent, int defaultValue, @Nullable Integer minValue, @Nullable Integer maxValue) {
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
    public Class<Integer> type() {
        return Integer.class;
    }

    @Override
    public Codec<Integer> codec() {
        return CODEC.validate(this::validateValue);
    }

    @Override
    public StreamCodec<ByteBuf, Integer> streamCodec() {
        return ByteBufCodecs.INT;
    }

    @Override
    public Integer defaultValue() {
        return defaultValue;
    }

    @Override
    public Optional<Integer> minValue() {
        return Optional.ofNullable(minValue);
    }

    @Override
    public Optional<Integer> maxValue() {
        return Optional.ofNullable(maxValue);
    }

    @Override
    public DataResult<Integer> validateValue(Integer value) {
        if (minValue != null && value < minValue) {
            return DataResult.error(() -> "Value for " + name() + " is below minimum " + minValue + ": " + value);
        }
        if (maxValue != null && value > maxValue) {
            return DataResult.error(() -> "Value for " + name() + " is above maximum " + maxValue + ": " + value);
        }
        return super.validateValue(value);
    }
}
