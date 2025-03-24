package net.blay09.mods.balm.common.client.rendering;

import com.mojang.math.Transformation;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractCachedDynamicModel implements BakedModel {

    private final Map<String, BakedModel> cache = new HashMap<>();
    private final Map<ModelResourceLocation, BakedModel> baseModelCache = new HashMap<>();
    private final Map<ModelResourceLocation, UnbakedModel> unbakedModels = new HashMap<>();

    private final ModelBakery modelBakery;
    private final Function<ModelResourceLocation, UnbakedModel> modelResolver;
    private final Function<BlockState, ModelResourceLocation> baseModelFunction;
    private final List<Pair<Predicate<BlockState>, BakedModel>> parts;
    private final Function<BlockState, Map<String, String>> textureMapFunction;
    private final BiConsumer<BlockState, Matrix4f> transformFunction;
    private final ModelResourceLocation location;
    private final ModelBakery.TextureGetter textureGetter;

    private TextureAtlasSprite particleTexture;

    public AbstractCachedDynamicModel(ModelBakery modelBakery, Map<ModelResourceLocation, UnbakedModel> models, Function<BlockState, ModelResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes, ModelResourceLocation location, ModelBakery.TextureGetter textureGetter) {
        this.modelBakery = modelBakery;
        unbakedModels.putAll(models);
        this.modelResolver = unbakedModels::get;
        this.baseModelFunction = baseModelFunction;
        this.parts = parts;
        this.textureMapFunction = textureMapFunction;
        this.transformFunction = transformFunction;
        this.location = location;
        this.textureGetter = textureGetter;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        if (state != null) {
            Matrix4f transform = BlockModelRotation.X0_Y0.getRotation().getMatrix();
            String stateString = state.toString();
            BakedModel bakedModel;
            synchronized (cache) {
                bakedModel = cache.get(stateString);
                if (bakedModel == null) {
                    if (transformFunction != null) {
                        transformFunction.accept(state, transform);
                    }

                    BalmModels models = BalmClient.getModels();
                    ModelState modelTransform = models.getModelState(new Transformation(transform));

                    ModelResourceLocation baseModelLocation = baseModelFunction.apply(state);

                    // If we're going to retexture, we need to ensure the base model has already been baked to prevent circular parent references in the retextured model
                    if (textureMapFunction != null && !baseModelCache.containsKey(baseModelLocation)) {
                        final UnbakedModel baseModel = modelResolver.apply(baseModelLocation);
                        final BakedModel bakedBaseModel = baseModel.bake(models.createBaker(baseModelLocation, textureGetter),
                                Material::sprite,
                                modelTransform);
                        baseModelCache.put(baseModelLocation, bakedBaseModel);
                    }

                    UnbakedModel retexturedBaseModel = textureMapFunction != null ? models.retexture(modelBakery,
                            baseModelLocation,
                            textureMapFunction.apply(state)) : modelResolver.apply(baseModelLocation);
                    bakedModel = retexturedBaseModel.bake(models.createBaker(location, textureGetter), Material::sprite, modelTransform);
                    cache.put(stateString, bakedModel);

                    if (particleTexture == null && bakedModel != null) {
                        particleTexture = bakedModel.getParticleIcon();
                    }
                }
            }

            return bakedModel != null ? bakedModel.getQuads(state, side, rand) : Collections.emptyList();
        }

        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particleTexture != null ? particleTexture : new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()).sprite();
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    public abstract List<RenderType> getBlockRenderTypes(BlockState state, RandomSource rand);

    public abstract List<RenderType> getItemRenderTypes(ItemStack itemStack, boolean fabulous);

}
