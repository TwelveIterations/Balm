package net.blay09.mods.balm.forge.client.model.item;

import net.blay09.mods.balm.client.model.item.BalmItemPropertyRegistrar;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ForgeBalmItemPropertyRegistrar implements BalmItemPropertyRegistrar {
    public static final ForgeBalmItemPropertyRegistrar INSTANCE = new ForgeBalmItemPropertyRegistrar();

    @Override
    public void register(ItemLike item, ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction) {
        ItemProperties.register(item.asItem(), identifier, propertyFunction);
    }

    @Override
    public void registerGeneric(ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction) {
        ItemProperties.registerGeneric(identifier, propertyFunction);
    }
}
