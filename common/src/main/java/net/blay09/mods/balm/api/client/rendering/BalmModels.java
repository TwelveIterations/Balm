package net.blay09.mods.balm.api.client.rendering;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmModels {
    DeferredObject<BakedModel> loadModel(ResourceLocation identifier);

    DeferredObject<BakedModel> bakeModel(ResourceLocation identifier, UnbakedModel model);

    default DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        return loadDynamicModel(identifier, modelFunction, textureMapFunction, transformFunction, Collections.emptyList());
    }

    DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes);

    DeferredObject<BakedModel> retexture(ResourceLocation identifier, Map<String, String> textureMap);

    void overrideModel(Supplier<Block> block, Supplier<BakedModel> model);

    ModelState getModelState(Transformation transformation);

    UnbakedModel getUnbakedModelOrMissing(ResourceLocation location);

    UnbakedModel getUnbakedMissingModel();

    default UnbakedModel retexture(ModelBakery bakery, ResourceLocation identifier, Map<String, String> textureMap) {
        Map<String, Either<Material, String>> replacedTexturesMapped = new HashMap<>();
        for (Map.Entry<String, String> entry : textureMap.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(entry.getValue()))));
        }

        BlockModel blockModel = new BlockModel(identifier,
                Collections.emptyList(),
                replacedTexturesMapped,
                false,
                BlockModel.GuiLight.FRONT,
                ItemTransforms.NO_TRANSFORMS,
                Collections.emptyList());

        // We have to resolve parents as that is usually done during stitching, which we're already past
        blockModel.resolveParents(bakery::getModel);

        return blockModel;
    }

    ModelBaker createBaker(ResourceLocation location, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction);

    BalmModels scoped(String modId);
}
