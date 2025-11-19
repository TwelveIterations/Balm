package net.blay09.mods.balm.neoforge.server.packs.resources.internal;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.server.packs.resources.BalmResourceCondition;
import net.blay09.mods.balm.server.packs.resources.internal.ResourceConditionContextImpl;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public record NeoForgeBalmResourceCondition<T extends BalmResourceCondition>(Identifier identifier, T delegate,
                                                                             Function<Identifier, @Nullable MapCodec<? extends ICondition>> codecResolver) implements ICondition {
    @Override
    public boolean test(IContext context) {
        return delegate.test(new ResourceConditionContextImpl(context));
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return Objects.requireNonNull(codecResolver.apply(identifier));
    }
}
