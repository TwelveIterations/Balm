package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface BalmRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    BalmConfig getConfig();

    BalmEvents getEvents();

    BalmWorldGen getWorldGen();

    @Deprecated
    default BalmBlocks getBlocks() {
        return BalmBlocks.LEGACY;
    }

    @Deprecated
    default BalmBlockEntities getBlockEntities() {
        return BalmBlockEntities.LEGACY;
    }

    @Deprecated
    default BalmItems getItems() {
        return BalmItems.LEGACY;
    }

    @Deprecated
    default BalmMenus getMenus() {
        return BalmMenus.LEGACY;
    }

    void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#menuTypes(String, Consumer)} instead.
     */
    @Deprecated
    BalmMenuTypeRegistrar menuTypes(String namespace);

    BalmNetworking getNetworking();

    BalmHooks getHooks();

    @Deprecated
    default BalmRegistries getRegistries() {
        return BalmRegistries.LEGACY;
    }

    @Deprecated
    default BalmSounds getSounds() {
        return BalmSounds.LEGACY;
    }

    /**
     * @deprecated Use {@link Balm#entityTypes(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    default BalmEntities getEntities() {
        return BalmEntities.LEGACY;
    }

    void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer);

    /**
     * @deprecated Use {@link Balm#entityTypes(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    BalmEntityTypeRegistrar entityTypes(String namespace);

    BalmCapabilities getCapabilities();

    BalmCommands getCommands();

    BalmLootTables getLootTables();

    @Deprecated
    default BalmStats getStats() {
        return BalmStats.LEGACY;
    }

    @Deprecated
    default BalmRecipes getRecipes() {
        return BalmRecipes.LEGACY;
    }

    BalmModSupport getModSupport();

    @Deprecated
    default BalmParticles getParticles() {
        return BalmParticles.LEGACY;
    }

    void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer);

    void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer);

    BalmPermissions getPermissions();

    @Deprecated
    default boolean isModLoaded(String modId) {
        return platform().isModLoaded(modId);
    }

    @Deprecated
    default String getModName(String modId) {
        return platform().getModName(modId);
    }

    <TProxy> SidedProxy<TProxy> sidedProxy(String commonName, String clientName);

    void initializeMod(String modId, TLoadContext context, Runnable initializer);

    void initializeIfLoaded(String modId, String className);

    @Deprecated
    default void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        Balm.resourceReloadListeners(identifier.getNamespace(), registrar -> {
            registrar.register(identifier.getPath(), reloadListener);
        });
    }

    @Deprecated
    default void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        Balm.resourceReloadListeners(identifier.getNamespace(), registrar -> {
            registrar.register(identifier.getPath(), reloadListener);
        });
    }

    @Deprecated
    default BalmComponents getComponents() {
        return BalmComponents.LEGACY;
    }

    <T> PlatformProxy<T> platformProxy();

    <T> ModProxy<T> modProxy();

    @Deprecated
    default String getPlatform() {
        return platform().name();
    }

    default void initializeModule(BalmModule module) {
        final var modId = module.getId().getNamespace();
        module.registerConfig(getConfig());

        module.registerResources(getResources());
        resourceConditions(modId, module::registerResourceConditions);

        module.registerAdditional(getRegistries());
        module.registerAdditional(registrar());

        module.registerComponents(getComponents());
        Balm.dataComponentTypes(modId, module::registerDataComponentTypes);

        module.registerBlocks(getBlocks().scoped(modId));
        blocks(modId, module::registerBlocks);

        module.registerBlockEntities(getBlockEntities());
        blockEntityTypes(modId, module::registerBlockEntityTypes);

        module.registerItems(getItems().scoped(modId));
        items(modId, module::registerItems);
        creativeModeTabs(modId, module::registerCreativeModeTabs);

        module.registerEntities(getEntities());
        entityTypes(modId, module::registerEntityTypes);
        module.registerWorldGen(getWorldGen());
        module.registerNetworking(getNetworking());
        module.registerMenus(getMenus());
        module.registerCapabilities(getCapabilities());
        module.registerCommands(getCommands());

        module.registerRecipes(getRecipes());
        recipeTypes(modId, module::registerRecipeTypes);

        module.registerLootTables(getLootTables());

        module.registerStats(getStats());
        customStats(modId, module::registerCustomStats);

        module.registerSounds(getSounds());
        module.registerSoundEvents(registrar().scoped(Registries.SOUND_EVENT, modId));

        module.registerPermissions(getPermissions());

        module.registerParticles(getParticles());
        particleTypes(modId, module::registerParticleTypes);

        resourceReloadListeners(modId, module::registerReloadListeners);

        module.registerEvents(getEvents());
        module.initialize();
    }

    BalmProxy getProxy();

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmModule module);

    @Deprecated
    default BalmResources getResources() {
        return BalmResources.LEGACY;
    }

    @Deprecated
    default BalmEnvironment getEnvironment() {
        return platform().physicalSide();
    }

    @Deprecated
    default boolean isDevelopmentEnvironment() {
        return platform().isDevelopmentEnvironment();
    }

    @Deprecated
    default List<String> getLoadedPrimaryModIds() {
        return platform().loadedPrimaryModIds();
    }

    @Deprecated
    default void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        platform().visitModResources(modId, path, visitor);
    }

    @Deprecated
    default Optional<ModResource> lookupModResource(String modId, String path) {
        return platform().lookupModResource(modId, path);
    }

    BalmRegistrar registrar();

    default <T> BalmRegistrar.Scoped<T> registrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return registrar().scoped(registryKey, namespace);
    }

    void blocks(String namespace, Consumer<BalmBlockRegistrar> initializer);

    void items(String namespace, Consumer<BalmItemRegistrar> initializer);

    void recipeTypes(String namespace, Consumer<BalmRecipeTypeRegistrar> initializer);

    @Deprecated
    BalmRecipeTypeRegistrar recipeTypes(String namespace);

    void dataComponentTypes(String namespace, Consumer<BalmDataComponentTypeRegistrar> initializer);

    /**
     * @deprecated Use {@link Balm#creativeModeTabs(String, Consumer)} instead.
     */
    @Deprecated
    BalmCreativeModeTabRegistrar creativeModeTabs(String namespace);

    void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer);

    /**
     * @deprecated Use {@link Balm#blockEntityTypes(String, Consumer)} instead.
     */
    @Deprecated
    BalmBlockEntityTypeRegistrar blockEntityTypes(String namespace);

    default void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(blockEntityTypes(namespace));
    }

    BalmPlatform platform();

    void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer);

    void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer);

    /**
     * @deprecated Use {@link Balm#particleTypes(String, Consumer)} instead.
     */
    @Deprecated
    BalmParticleTypeRegistrar particleTypes(String namespace);

    @Deprecated
    BalmCustomStatRegistrar customStats(String namespace);
}
