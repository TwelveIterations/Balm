package net.blay09.mods.balm.api.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockStateModels(String, Consumer)} instead.
 */
@Deprecated
public interface BalmModels {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockStateModels(String, Consumer)} instead.
     */
    @Deprecated
    DeferredObject<BlockStateModel> loadModel(Identifier identifier);

    @Deprecated
    default BalmModels scoped(String modId) {
        return this;
    }
}
