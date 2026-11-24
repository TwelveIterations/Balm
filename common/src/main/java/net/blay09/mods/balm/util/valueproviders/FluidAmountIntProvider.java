package net.blay09.mods.balm.util.valueproviders;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.platform.fluid.FluidAmount;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

/** A constant fluid quantity evaluated in the platform's native unit. */
public record FluidAmountIntProvider(FluidAmount amount) implements IntProvider, ContextIntProvider {
    public static final MapCodec<FluidAmountIntProvider> MAP_CODEC = FluidAmount.MAP_CODEC.xmap(FluidAmountIntProvider::new, FluidAmountIntProvider::amount);

    private int value() {
        return Math.toIntExact(amount.toPlatform());
    }

    @Override
    public int sample(RandomSource random) {
        return value();
    }

    @Override
    public int minInclusive() {
        return value();
    }

    @Override
    public int maxInclusive() {
        return value();
    }

    @Override
    public int getIntUnsafe(LootContext context) {
        return value();
    }

    @Override
    public void validate(ValidationContext context) {
    }

    @Override
    public MapCodec<FluidAmountIntProvider> codec() {
        return MAP_CODEC;
    }
}
