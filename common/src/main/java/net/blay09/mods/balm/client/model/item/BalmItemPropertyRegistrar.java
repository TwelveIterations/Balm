package net.blay09.mods.balm.client.model.item;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public interface BalmItemPropertyRegistrar {

    void register(ItemLike item, ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction);

    void registerGeneric(ResourceLocation identifier, ClampedItemPropertyFunction propertyFunction);
}
