package net.blay09.mods.balm.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

import java.util.Map;

public interface NumberProvider {

    record Constant(float value) implements NumberProvider {
        public static MapCodec<Constant> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("value").forGetter(Constant::value)
        ).apply(instance, Constant::new));

        @Override
        public float getFloat(RandomSource random) {
            return value;
        }

        @Override
        public String type() {
            return "constant";
        }

        @Override
        public MapCodec<? extends NumberProvider> codec() {
            return CODEC;
        }

        public Constant mul(float scale) {
            return new Constant(value * scale);
        }
    }

    record Uniform(float min, float max) implements NumberProvider {
        public static MapCodec<Uniform> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.fieldOf("min").forGetter(Uniform::min),
                Codec.FLOAT.fieldOf("max").forGetter(Uniform::max)
        ).apply(instance, Uniform::new));

        @Override
        public float getFloat(RandomSource random) {
            return min + random.nextFloat() * (max - min);
        }

        @Override
        public String type() {
            return "uniform";
        }

        @Override
        public MapCodec<? extends NumberProvider> codec() {
            return CODEC;
        }

        public Uniform mul(float scale) {
            return mul(scale, scale);
        }

        public Uniform mul(float minScale, float maxScale) {
            return new Uniform(min * minScale, max * maxScale);
        }
    }

    Map<String, MapCodec<? extends NumberProvider>> CODECS = Map.of(
            "constant", Constant.CODEC,
            "uniform", Uniform.CODEC
    );

    Codec<NumberProvider> CODEC = Codec.STRING.dispatch(NumberProvider::type, CODECS::get);

    static Constant constant(float value) {
        return new Constant(value);
    }

    static Uniform uniform(float min, float max) {
        return new Uniform(min, max);
    }

    float getFloat(RandomSource random);

    default int getInt(RandomSource random) {
        return Math.round(getFloat(random));
    }

    String type();

    MapCodec<? extends NumberProvider> codec();
}
