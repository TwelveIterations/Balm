package net.blay09.mods.balm.api.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#blockStateModels(String, Consumer)} instead.
 */
@Deprecated
public interface BalmModels {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.client.BalmClient#blockStateModels(String, Consumer)} instead.
     */
    @Deprecated
    DeferredObject<BlockStateModel> loadModel(ResourceLocation identifier);

    @Deprecated
    default BalmModels scoped(String modId) {
        return this;
    }
}
