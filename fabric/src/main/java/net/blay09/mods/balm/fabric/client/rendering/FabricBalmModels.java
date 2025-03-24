package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FabricBalmModels implements BalmModels, ModelLoadingPlugin {

    private final List<ResourceLocation> additionalModels = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void initialize(Context context) {
        // TODO 1.21.5: context.addModels(additionalModels); not yet implemented in Fabric
    }

    @Override
    public DeferredObject<BlockStateModel> loadModel(final ResourceLocation identifier) {
        final var deferredObject = new DeferredObject<BlockStateModel>(identifier) {
            @Override
            public BlockStateModel resolve() {
                return Minecraft.getInstance().getModelManager().getMissingBlockStateModel();
            }

            @Override
            public boolean canResolve() {
                return true; // TODO 1.21.5: We just resolve to missing model for now
            }
        };
        additionalModels.add(identifier);
        return deferredObject;
    }

}
