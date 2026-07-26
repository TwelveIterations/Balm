package net.blay09.mods.balm.platform.config.schema.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.schema.ConfiguredLong;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class LongConfigProperty extends AbstractConfigProperty<Long> implements ConfiguredLong {
    public static final Codec<Long> CODEC = Codec.withAlternative(Codec.LONG, Codec.STRING.xmap(Long::parseLong, String::valueOf));
    private final long defaultValue;
    private final @Nullable Long minValue;
    private final @Nullable Long maxValue;

    public LongConfigProperty(ConfigPropertyBuilder parent, long defaultValue) {
        this(parent, defaultValue, null, null);
    }

    public LongConfigProperty(ConfigPropertyBuilder parent, long defaultValue, long minValue, long maxValue) {
        this(parent, defaultValue, Long.valueOf(minValue), Long.valueOf(maxValue));
    }

    private LongConfigProperty(ConfigPropertyBuilder parent, long defaultValue, @Nullable Long minValue, @Nullable Long maxValue) {
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
    public Class<Long> type() {
        return Long.class;
    }

    @Override
    public Codec<Long> codec() {
        return CODEC.validate(this::validateValue);
    }

    @Override
    public StreamCodec<ByteBuf, Long> streamCodec() {
        return ByteBufCodecs.LONG;
    }

    @Override
    public Long defaultValue() {
        return defaultValue;
    }

    @Override
    public Optional<Long> minValue() {
        return Optional.ofNullable(minValue);
    }

    @Override
    public Optional<Long> maxValue() {
        return Optional.ofNullable(maxValue);
    }

    @Override
    public DataResult<Long> validateValue(Long value) {
        if (minValue != null && value < minValue) {
            return DataResult.error(() -> "Value for " + name() + " is below minimum " + minValue + ": " + value);
        }
        if (maxValue != null && value > maxValue) {
            return DataResult.error(() -> "Value for " + name() + " is above maximum " + maxValue + ": " + value);
        }
        return super.validateValue(value);
    }
}
