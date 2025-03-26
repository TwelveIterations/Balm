package net.blay09.mods.balm.fabric.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.BalmResourceCondition;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class FabricBalmResources implements BalmResources {
    private final Map<ResourceLocation, ResourceConditionType<?>> conditions = new HashMap<>();

    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var type = ResourceConditionType.create(identifier, codec
                .xmap(it -> new FabricBalmResourceCondition<>(identifier, it, conditions::get),
                        FabricBalmResourceCondition::delegate));
        ResourceConditions.register(type);
        conditions.put(identifier, type);
    }

}
