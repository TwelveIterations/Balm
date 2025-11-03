package net.blay09.mods.balm.fabric.resources;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.resources.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class FabricBalmResources implements BalmResources {
    private final Map<ResourceLocation, ResourceConditionType<?>> conditions = new HashMap<>();

    @Override
    public <T extends BalmResourceCondition> void registerResourceCondition(ResourceLocation identifier, MapCodec<T> codec) {
        final var type = ResourceConditionType.create(identifier, codec
                .xmap(it -> new FabricBalmResourceCondition<>(identifier, it, conditions::get),
                        FabricBalmResourceCondition::delegate));
        ResourceConditions.register(type);
        conditions.put(identifier, type);
    }

    @Override
    public void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        FabricLoader.getInstance().getModContainer(modId)
                .flatMap(modContainer -> modContainer.findPath(path))
                .ifPresent(rootPath -> {
                    try (final var walker = Files.walk(rootPath)) {
                        walker.forEach(childPath -> visitor.visit(new PathModResource(childPath)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public Optional<ModResource> lookupModResource(String modId, String path) {
        return FabricLoader.getInstance().getModContainer(modId)
                .flatMap(modContainer -> modContainer.findPath(path))
                .map(PathModResource::new);
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> factory) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(identifier, providers -> new IdentifiableResourceReloadListener() {
            private final PreparableReloadListener listener = factory.apply(providers);

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2) {
                return listener.reload(sharedState, executor, preparationBarrier, executor2);
            }
        });
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                reloadListener.accept(resourceManager);
            }

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }
        });
    }

}
