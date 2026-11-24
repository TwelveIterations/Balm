package net.blay09.mods.balm.util.valueproviders;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.platform.fluid.FluidAmount;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public record FluidAmountFloatProvider(FluidAmount amount) implements FloatProvider, ContextFloatProvider {
    public static final MapCodec<FluidAmountFloatProvider> MAP_CODEC = FluidAmount.MAP_CODEC.xmap(FluidAmountFloatProvider::new, FluidAmountFloatProvider::amount);

    private float value() {
        return amount.toPlatform();
    }

    @Override
    public float sample(RandomSource random) {
        return value();
    }

    @Override
    public float min() {
        return value();
    }

    @Override
    public float max() {
        return value();
    }

    @Override
    public float getFloatUnsafe(LootContext context) {
        return value();
    }

    @Override
    public void validate(ValidationContext context) {
    }

    @Override
    public MapCodec<FluidAmountFloatProvider> codec() {
        return MAP_CODEC;
    }
}
