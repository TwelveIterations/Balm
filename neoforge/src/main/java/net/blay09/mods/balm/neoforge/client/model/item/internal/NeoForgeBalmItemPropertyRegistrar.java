package net.blay09.mods.balm.neoforge.client.model.item.internal;

import net.blay09.mods.balm.client.model.item.BalmItemPropertyRegistrar;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class NeoForgeBalmItemPropertyRegistrar implements BalmItemPropertyRegistrar {
    public static final NeoForgeBalmItemPropertyRegistrar INSTANCE = new NeoForgeBalmItemPropertyRegistrar();

    @Override
    public void register(ItemLike item, ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction) {
        ItemProperties.register(item.asItem(), identifier, propertyFunction);
    }

    @Override
    public void registerGeneric(ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction) {
        ItemProperties.registerGeneric(identifier, propertyFunction);
    }
}
