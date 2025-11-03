package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.item.BalmItemFactory;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeFactory;
import net.blay09.mods.balm.world.level.block.BalmBlockFactory;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeFactory;
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

    BalmMenus getMenus();

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

    BalmEntities getEntities();

    BalmCapabilities getCapabilities();

    BalmCommands getCommands();

    BalmLootTables getLootTables();

    BalmStats getStats();

    BalmRecipes getRecipes();

    BalmModSupport getModSupport();

    BalmParticles getParticles();

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

    void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener);

    void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener);

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

        module.registerAdditional(getRegistries());
        module.registerAdditional(registrar());

        module.registerComponents(getComponents());
        module.registerDataComponentTypes(registrar().scoped(Registries.DATA_COMPONENT_TYPE, modId));

        module.registerBlocks(getBlocks().scoped(modId));
        blocks(modId, module::registerBlocks);

        module.registerBlockEntities(getBlockEntities());
        blockEntityTypes(modId, module::registerBlockEntityTypes);

        module.registerItems(getItems().scoped(modId));
        items(modId, module::registerItems);
        creativeModeTabs(modId, module::registerCreativeModeTabs);

        module.registerEntities(getEntities());
        module.registerWorldGen(getWorldGen());
        module.registerNetworking(getNetworking());
        module.registerMenus(getMenus());
        module.registerCapabilities(getCapabilities());
        module.registerCommands(getCommands());

        module.registerRecipes(getRecipes());
        recipeTypes(modId, module::registerRecipeTypes);

        module.registerLootTables(getLootTables());
        module.registerStats(getStats());

        module.registerSounds(getSounds());
        module.registerSoundEvents(registrar().scoped(Registries.SOUND_EVENT, modId));

        module.registerPermissions(getPermissions());
        module.registerParticles(getParticles());
        module.registerEvents(getEvents());
        module.initialize();
    }

    BalmProxy getProxy();

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmModule module);

    BalmResources getResources();

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
        getResources().visitModResources(modId, path, visitor);
    }

    @Deprecated
    default Optional<ModResource> lookupModResource(String modId, String path) {
        return getResources().lookupModResource(modId, path);
    }

    BalmRegistrar registrar();

    default <T> BalmRegistrar.Scoped<T> registrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return registrar().scoped(registryKey, namespace);
    }

    void blocks(String namespace, Consumer<BalmBlockFactory> initializer);

    void items(String namespace, Consumer<BalmItemFactory> initializer);

    void recipeTypes(String namespace, Consumer<BalmRecipeTypeFactory> initializer);

    /**
     * @deprecated Use {@link Balm#creativeModeTabs(String, Consumer)} instead.
     */
    @Deprecated
    BalmCreativeModeTabFactory creativeModeTabs(String namespace);

    void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabFactory> initializer);

    /**
     * @deprecated Use {@link Balm#blockEntityTypes(String, Consumer)} instead.
     */
    @Deprecated
    BalmBlockEntityTypeFactory blockEntityTypes(String namespace);

    default void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeFactory> initializer) {
        initializer.accept(blockEntityTypes(namespace));
    }

    BalmPlatform platform();
}
