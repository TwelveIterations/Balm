package net.blay09.mods.balm.fabric.client.internal.model.item;

import net.blay09.mods.balm.client.model.item.BalmItemPropertyRegistrar;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class FabricBalmItemPropertyRegistrar implements BalmItemPropertyRegistrar {
    public static final FabricBalmItemPropertyRegistrar INSTANCE = new FabricBalmItemPropertyRegistrar();

    @Override
    public void register(ItemLike item, ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction) {
        FabricModelPredicateProviderRegistry.register(item.asItem(), identifier, propertyFunction);
    }

    @Override
    public void registerGeneric(ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction) {
        FabricModelPredicateProviderRegistry.register(identifier, propertyFunction);
    }
}
