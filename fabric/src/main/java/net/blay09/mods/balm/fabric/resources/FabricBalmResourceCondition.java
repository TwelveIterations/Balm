package net.blay09.mods.balm.fabric.resources;

import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.common.resources.ResourceConditionContextImpl;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record FabricBalmResourceCondition<T extends BalmResourceCondition>(Identifier identifier, T delegate,
                                          Function<Identifier, ResourceConditionType<?>> typeResolver) implements ResourceCondition {
    @Override
    public ResourceConditionType<?> getType() {
        return typeResolver.apply(identifier);
    }

    @Override
    public boolean test(RegistryOps.@Nullable RegistryInfoLookup registryInfoLookup) {
        return delegate.test(new ResourceConditionContextImpl(registryInfoLookup));
    }
}
