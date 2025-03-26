package net.blay09.mods.balm.fabric.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.common.resources.ResourceConditionContextImpl;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FabricBalmResources implements BalmResources {
    private final Map<ResourceLocation, ResourceConditionType<?>> conditions = new HashMap<>();

    @Override
    public void registerResourceCondition(ResourceLocation identifier, MapCodec<BalmResourceCondition> codec) {
        final var type = ResourceConditionType.create(identifier, codec.xmap(it -> new ResourceCondition() {
            @Override
            public ResourceConditionType<?> getType() {
                return conditions.get(identifier);
            }

            @Override
            public boolean test(@Nullable RegistryOps.RegistryInfoLookup registryInfoLookup) {
                return it.test(new ResourceConditionContextImpl(registryInfoLookup));
            }
        }, it -> (BalmResourceCondition) context -> it.test((RegistryOps.RegistryInfoLookup) context.backingContext())));
        ResourceConditions.register(type);
        conditions.put(identifier, type);
    }

}
