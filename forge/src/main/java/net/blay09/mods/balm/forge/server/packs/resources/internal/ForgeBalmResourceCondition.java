package net.blay09.mods.balm.forge.server.packs.resources.internal;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.server.packs.resources.BalmResourceCondition;
import net.blay09.mods.balm.server.packs.resources.internal.ResourceConditionContextImpl;
import net.minecraft.resources.Identifier;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.function.Function;

public record ForgeBalmResourceCondition<T extends BalmResourceCondition>(Identifier identifier, T delegate,
                                                                          Function<Identifier, MapCodec<? extends ICondition>> codecResolver) implements ICondition {
    @Override
    public boolean test(IContext context, DynamicOps<?> ops) {
        return delegate.test(new ResourceConditionContextImpl(context));
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return codecResolver.apply(identifier);
    }
}
