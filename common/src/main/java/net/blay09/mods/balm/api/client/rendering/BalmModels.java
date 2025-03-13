package net.blay09.mods.balm.api.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

public interface BalmModels {
    DeferredObject<BlockStateModel> loadModel(ResourceLocation identifier);
}
