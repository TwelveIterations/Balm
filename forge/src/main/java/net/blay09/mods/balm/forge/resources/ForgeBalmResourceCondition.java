package net.blay09.mods.balm.forge.resources;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.common.resources.ResourceConditionContextImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.function.Function;

public record ForgeBalmResourceCondition<T extends BalmResourceCondition>(ResourceLocation identifier, T delegate,
                                                                          Function<ResourceLocation, MapCodec<? extends ICondition>> codecResolver) implements ICondition {
    @Override
    public boolean test(IContext context, DynamicOps<?> ops) {
        return delegate.test(new ResourceConditionContextImpl(context));
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return codecResolver.apply(identifier);
    }
}
