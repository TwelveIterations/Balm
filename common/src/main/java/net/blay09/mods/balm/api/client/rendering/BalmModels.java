package net.blay09.mods.balm.api.client.rendering;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.mixin.ModelBakeryAccessor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmModels {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockStateModels(Consumer)} instead.
     */
    @Deprecated
    DeferredObject<BakedModel> loadModel(ResourceLocation identifier);

    DeferredObject<BakedModel> bakeModel(ModelResourceLocation identifier, UnbakedModel model);

    default DeferredObject<BakedModel> loadDynamicModel(ModelResourceLocation identifier, Set<ModelResourceLocation> models, @Nullable Function<BlockState, ModelResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        return loadDynamicModel(identifier, models, modelFunction, textureMapFunction, transformFunction, Collections.emptyList());
    }

    DeferredObject<BakedModel> loadDynamicModel(ModelResourceLocation identifier, Set<ModelResourceLocation> models, @Nullable Function<BlockState, ModelResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes);

    DeferredObject<BakedModel> retexture(ModelResourceLocation identifier, Map<String, String> textureMap);

    void overrideModel(Supplier<Block> block, Supplier<BakedModel> model);

    ModelState getModelState(Transformation transformation);

    UnbakedModel getUnbakedModelOrMissing(ResourceLocation location);

    UnbakedModel getUnbakedMissingModel();

    default UnbakedModel retexture(ModelBakery bakery, ModelResourceLocation identifier, Map<String, String> textureMap) {
        Map<String, Either<Material, String>> replacedTexturesMapped = new HashMap<>();
        for (Map.Entry<String, String> entry : textureMap.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse(entry.getValue()))));
        }

        BlockModel blockModel = new BlockModel(identifier.id(),
                Collections.emptyList(),
                replacedTexturesMapped,
                false,
                BlockModel.GuiLight.FRONT,
                ItemTransforms.NO_TRANSFORMS,
                Collections.emptyList());

        // We have to resolve parents as that is usually done during stitching, which we're already past
        blockModel.resolveParents(it -> ((ModelBakeryAccessor) bakery).callGetModel(it));

        return blockModel;
    }

    ModelBaker createBaker(ModelResourceLocation location, ModelBakery.TextureGetter textureGetter);

    @Deprecated
    BalmModels scoped(String modId);
}
