package net.blay09.mods.balm.fabric.client.rendering;

import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.mixin.ModelBakeryAccessor;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record FabricBalmModels(NamespaceResolver namespaceResolver) implements BalmModels, ModelLoadingPlugin {

    private static final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = Collections.synchronizedList(new ArrayList<>());
    private static final List<ResourceLocation> additionalModels = Collections.synchronizedList(new ArrayList<>());
    private static final List<DeferredModel> modelsToBake = Collections.synchronizedList(new ArrayList<>());
    private static ModelBakery modelBakery;

    public static void onBakeModels(ModelBakery modelBakery, ModelBakery.TextureGetter textureGetter) {
        FabricBalmModels.modelBakery = modelBakery;

        synchronized (modelsToBake) {
            for (DeferredModel model : modelsToBake) {
                model.set(model.resolve(modelBakery, textureGetter));
            }
        }

        synchronized (overrides) {
            for (Pair<Supplier<Block>, Supplier<BakedModel>> override : overrides) {
                Block block = override.getFirst().get();
                BakedModel bakedModel = override.getSecond().get();
                block.getStateDefinition().getPossibleStates().forEach((state) -> {
                    ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
                    modelBakery.getBakedTopLevelModels().put(modelLocation, bakedModel);
                });
            }
        }
    }

    @Override
    public void onInitializeModelLoader(Context context) {
        context.addModels(additionalModels);
    }

    @Override
    public DeferredObject<BakedModel> loadModel(final ResourceLocation identifier) {
        // fabric_resource is what Fabric uses as variant for additional models
        final var modelResourceLocation = new ModelResourceLocation(identifier, "fabric_resource");
        DeferredModel deferredModel = new DeferredModel(modelResourceLocation) {
            @Override
            public BakedModel resolve(ModelBakery modelBakery, ModelBakery.TextureGetter textureGetter) {
                return resolve();
            }

            @Override
            public BakedModel resolve() {
                return modelBakery.getBakedTopLevelModels().get(modelResourceLocation);
            }

            @Override
            public boolean canResolve() {
                return modelBakery.getBakedTopLevelModels().containsKey(modelResourceLocation);
            }
        };
        additionalModels.add(identifier);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(ModelResourceLocation identifier, UnbakedModel model) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, ModelBakery.TextureGetter textureGetter) {
                ModelBaker baker = createBaker(identifier, textureGetter);
                Function<Material, TextureAtlasSprite> modelTextureGetter = createModelTextureGetter(identifier, textureGetter);
                return model.bake(baker, modelTextureGetter, getModelState(Transformation.identity()));
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(ModelResourceLocation identifier, Set<ModelResourceLocation> models, @Nullable Function<BlockState, ModelResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes) {
        Function<BlockState, ModelResourceLocation> effectiveModelFunction = modelFunction != null ? modelFunction : (it -> identifier);
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, ModelBakery.TextureGetter textureGetter) {
                final var unbakedModels = new HashMap<ModelResourceLocation, UnbakedModel>();
                for (final var modelId : models) {
                    unbakedModels.put(modelId, ((ModelBakeryAccessor) bakery).callGetModel(modelId.id()));
                }
                return new FabricCachedDynamicModel(bakery,
                        unbakedModels,
                        effectiveModelFunction,
                        null,
                        textureMapFunction,
                        transformFunction,
                        renderTypes,
                        identifier,
                        textureGetter);
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> retexture(ModelResourceLocation identifier, Map<String, String> textureMap) {
        DeferredModel deferredModel = new DeferredModel(identifier) {
            @Override
            public BakedModel resolve(ModelBakery bakery, ModelBakery.TextureGetter textureGetter) {
                UnbakedModel model = retexture(bakery, identifier, textureMap);
                ModelBaker baker = createBaker(identifier, textureGetter);
                Function<Material, TextureAtlasSprite> modelTextureGetter = createModelTextureGetter(identifier, textureGetter);
                return model.bake(baker, modelTextureGetter, getModelState(Transformation.identity()));
            }
        };
        modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
        overrides.add(Pair.of(block, model));
    }

    @Override
    public ModelState getModelState(Transformation transformation) {
        return new TransformationModelState(transformation);
    }

    @Override
    public UnbakedModel getUnbakedModelOrMissing(ResourceLocation location) {
        return ((ModelBakeryAccessor) modelBakery).callGetModel(location);
    }

    @Override
    public UnbakedModel getUnbakedMissingModel() {
        return ((ModelBakeryAccessor) modelBakery).callGetModel(ModelBakery.MISSING_MODEL_LOCATION);
    }

    @Override
    public ModelBaker createBaker(ModelResourceLocation location, ModelBakery.TextureGetter textureGetter) {
        try {
            MappingResolver mappings = FabricLoader.getInstance().getMappingResolver();
            Class<?> clazz = Class.forName(mappings.mapClassName("intermediary", "net.minecraft.class_1088$class_7778"));
            Constructor<?> constructor = clazz.getDeclaredConstructor(ModelBakery.class, ModelBakery.TextureGetter.class, ModelResourceLocation.class);
            constructor.setAccessible(true);
            return (ModelBaker) constructor.newInstance(modelBakery, textureGetter, location);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Balm failed to create model baker", e);
        }
    }

    @Override
    public BalmModels scoped(String modId) {
        return new FabricBalmModels(new StaticNamespaceResolver(modId));
    }

    private Function<Material, TextureAtlasSprite> createModelTextureGetter(ModelResourceLocation location, ModelBakery.TextureGetter textureGetter) {
        return (Material material) -> (TextureAtlasSprite) textureGetter.get(location, material);
    }

    private static abstract class DeferredModel extends DeferredObject<BakedModel> {
        public DeferredModel(ModelResourceLocation identifier) {
            super(identifier.id());
        }

        public abstract BakedModel resolve(ModelBakery modelBakery, ModelBakery.TextureGetter textureGetter);

        @Override
        public void set(BakedModel object) {
            super.set(object);
        }
    }
}
