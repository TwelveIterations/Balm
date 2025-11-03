package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntityTypeFactory;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenuTypeFactory;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.common.permission.CommonBalmPermissions;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.fabric.capability.FabricBalmCapabilities;
import net.blay09.mods.balm.fabric.command.FabricBalmCommands;
import net.blay09.mods.balm.fabric.compat.FabricBalmModSupport;
import net.blay09.mods.balm.fabric.config.FabricBalmConfig;
import net.blay09.mods.balm.fabric.entity.FabricBalmEntityTypeFactory;
import net.blay09.mods.balm.fabric.event.FabricBalmCommonEvents;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.level.block.entity.FabricBalmBlockEntityTypeFactory;
import net.blay09.mods.balm.fabric.loader.FabricBalmPlatform;
import net.blay09.mods.balm.fabric.menu.FabricBalmMenuTypeFactory;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.particle.FabricBalmParticles;
import net.blay09.mods.balm.fabric.recipe.FabricBalmRecipes;
import net.blay09.mods.balm.fabric.core.FabricBalmRegistrar;
import net.blay09.mods.balm.fabric.resources.FabricBalmResources;
import net.blay09.mods.balm.fabric.stats.FabricBalmStats;
import net.blay09.mods.balm.fabric.world.FabricBalmWorldGen;
import net.blay09.mods.balm.fabric.world.item.FabricBalmCreativeModeTabFactory;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeFactory;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmRuntime extends CommonBalmRuntime<EmptyLoadContext> {
    private final BalmWorldGen worldGen = new FabricBalmWorldGen();
    private final FabricBalmEvents events = new FabricBalmEvents();
    private final BalmNetworking networking = new FabricBalmNetworking();
    private final BalmConfig config = new FabricBalmConfig();
    private final BalmHooks hooks = new FabricBalmHooks();
    private final BalmRegistrar registrar = new FabricBalmRegistrar();
    private final BalmCapabilities capabilities = new FabricBalmCapabilities();
    private final BalmCommands commands = new FabricBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new FabricBalmStats();
    private final BalmRecipes recipes = new FabricBalmRecipes();
    private final BalmModSupport modSupport = new FabricBalmModSupport(this);
    private final BalmParticles particles = new FabricBalmParticles();
    private final BalmPlatform platform = new FabricBalmPlatform();
    private final Supplier<BalmPermissions> permissions = this.<BalmPermissions>modProxy()
            .with("fabric-permissions-api-v0", "net.blay09.mods.balm.fabric.compat.FabricPermissionsAPIIntegration")
            .withFallback(new CommonBalmPermissions())
            .buildLazily();
    private final BalmResources resources = new FabricBalmResources();

    public FabricBalmRuntime() {
        FabricBalmCommonEvents.registerEvents(events);
    }

    @Override
    public BalmConfig getConfig() {
        return config;
    }

    @Override
    public BalmEvents getEvents() {
        return events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return worldGen;
    }

    @Override
    public BalmNetworking getNetworking() {
        return networking;
    }

    @Override
    public BalmHooks getHooks() {
        return hooks;
    }

    @Override
    public BalmCapabilities getCapabilities() {
        return capabilities;
    }

    @Override
    public BalmCommands getCommands() {
        return commands;
    }

    @Override
    public BalmLootTables getLootTables() {
        return lootTables;
    }

    @Override
    public BalmStats getStats() {
        return stats;
    }

    @Override
    public BalmRecipes getRecipes() {
        return recipes;
    }

    @Override
    public void initializeMod(String modId, EmptyLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();
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

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
    }

    @Override
    public BalmParticles getParticles() {
        return particles;
    }

    @Override
    public BalmPermissions getPermissions() {
        return permissions.get();
    }

    @Override
    public BalmResources getResources() {
        return resources;
    }

    @Override
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabFactory> initializer) {
        initializer.accept(new FabricBalmCreativeModeTabFactory(registrar(), namespace));
    }

    @Override
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeFactory> initializer) {
        initializer.accept(new FabricBalmBlockEntityTypeFactory(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeFactory> initializer) {
        initializer.accept(new FabricBalmMenuTypeFactory(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, java.util.function.Consumer<BalmEntityTypeFactory> initializer) {
        initializer.accept(new FabricBalmEntityTypeFactory(registrar(), namespace));
    }

    @Override
    @Deprecated
    public BalmEntityTypeFactory entityTypes(String namespace) {
        return new FabricBalmEntityTypeFactory(registrar(), namespace);
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    @Deprecated
    public BalmCreativeModeTabFactory creativeModeTabs(String namespace) {
        return new FabricBalmCreativeModeTabFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmBlockEntityTypeFactory blockEntityTypes(String namespace) {
        return new FabricBalmBlockEntityTypeFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmMenuTypeFactory menuTypes(String namespace) {
        return new FabricBalmMenuTypeFactory(registrar(), namespace);
    }
}
