package net.blay09.mods.balm.fabric.resources;

import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.common.resources.ResourceConditionContextImpl;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record FabricBalmResourceCondition<T extends BalmResourceCondition>(ResourceLocation identifier, T delegate,
                                          Function<ResourceLocation, ResourceConditionType<?>> typeResolver) implements ResourceCondition {
    @Override
    public ResourceConditionType<?> getType() {
        return typeResolver.apply(identifier);
    }

    @Override
    public boolean test(@Nullable HolderLookup.Provider registryLookup) {
        return delegate.test(new ResourceConditionContextImpl(registryLookup));
    }
}
