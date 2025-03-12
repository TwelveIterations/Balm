package net.blay09.mods.balm.api.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;

public interface BalmModels {
    DeferredObject<ItemModel> loadModel(ResourceLocation identifier);
}
