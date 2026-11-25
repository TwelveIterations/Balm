package net.blay09.mods.balm.platform.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blay09.mods.balm.Balm;
import net.minecraft.util.StringRepresentable;

import java.math.BigInteger;
import java.util.Locale;

public record FluidAmount(long amount, Unit unit) {
    public static final MapCodec<FluidAmount> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.LONG.fieldOf("value").forGetter(FluidAmount::amount),
            Unit.CODEC.fieldOf("unit").forGetter(FluidAmount::unit)
    ).apply(instance, FluidAmount::new));
    public static final Codec<FluidAmount> CODEC = MAP_CODEC.codec();

    public static FluidAmount buckets(long amount) {
        return new FluidAmount(amount, Unit.BUCKETS);
    }

    public static FluidAmount bottles(long amount) {
        return new FluidAmount(amount, Unit.BOTTLES);
    }

    public static FluidAmount millibuckets(long amount) {
        return new FluidAmount(amount, Unit.MILLIBUCKETS);
    }

    public static FluidAmount droplets(long amount) {
        return new FluidAmount(amount, Unit.DROPLETS);
    }

    public FluidAmount to(Unit target) {
        return new FluidAmount(unit.convert(amount, target), target);
    }

    public long toPlatform() {
        return unit.convert(amount, Unit.platform());
    }

    public enum Unit implements StringRepresentable {
        BUCKETS(1),
        MILLIBUCKETS(1000),
        DROPLETS(81000),
        BOTTLES(0) {
            @Override
            protected long perBucket() {
                return platform() == DROPLETS ? 3 : 4;
            }
        };

        public static final Codec<Unit> CODEC = StringRepresentable.fromEnum(Unit::values);

        private final long perBucket;

        Unit(long perBucket) {
            this.perBucket = perBucket;
        }

        protected long perBucket() {
            return perBucket;
        }

        public long convert(long amount, Unit target) {
            if (this == target) {
                return amount;
            }

            return BigInteger.valueOf(amount)
                    .multiply(BigInteger.valueOf(target.perBucket()))
                    .divide(BigInteger.valueOf(perBucket()))
                    .longValueExact();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public static Unit platform() {
            return Balm.platform().fluidUnit();
        }
    }
}
