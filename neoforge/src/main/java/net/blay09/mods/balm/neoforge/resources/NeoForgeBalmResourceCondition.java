package net.blay09.mods.balm.neoforge.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.common.resources.ResourceConditionContextImpl;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.function.Function;

public record NeoForgeBalmResourceCondition<T extends BalmResourceCondition>(Identifier identifier, T delegate,
                                                                             Function<Identifier, MapCodec<? extends ICondition>> codecResolver) implements ICondition {
    @Override
    public boolean test(IContext context) {
        return delegate.test(new ResourceConditionContextImpl(context));
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return codecResolver.apply(identifier);
    }
}
