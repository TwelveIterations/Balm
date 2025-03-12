package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class FabricBalmModels implements BalmModels, ModelLoadingPlugin {

    private final List<ResourceLocation> additionalModels = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void initialize(Context context) {
        // TODO context.addModels(additionalModels); is this even needed if we use item models?
    }

    @Override
    public DeferredObject<ItemModel> loadModel(final ResourceLocation identifier) {
        final var deferredObject = new DeferredObject<ItemModel>(identifier) {
            @Override
            public ItemModel resolve() {
                return Minecraft.getInstance().getModelManager().getItemModel(identifier);
            }

            @Override
            public boolean canResolve() {
                final var modelManager = Minecraft.getInstance().getModelManager();
                final var foundModel = modelManager.getItemModel(identifier);
                return foundModel != modelManager.getMissingBlockStateModel();
            }
        };
        additionalModels.add(identifier);
        return deferredObject;
    }

}
