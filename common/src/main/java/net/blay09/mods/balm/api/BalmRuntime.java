package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.event.BidirectionalEventMapper;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

public interface BalmRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    BalmConfig getConfig();

    BalmEvents getEvents();

    BalmWorldGen getWorldGen();

    void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer);

    BalmNetworking getNetworking();

    BalmHooks getHooks();

    void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer);

    BalmCapabilities getCapabilities();

    BalmCommands getCommands();

    BalmLootTables getLootTables();

    BalmModSupport getModSupport();

    void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer);

    void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer);

    BalmPermissions getPermissions();

    <TProxy> SidedProxy<TProxy> sidedProxy(String commonName, String clientName);

    void initializeMod(String modId, TLoadContext context, Consumer<BalmRegistrars> initializer);

    void initializeIfLoaded(String modId, String className);

    <T> PlatformProxy<T> platformProxy();

    <T> ModProxy<T> modProxy();

    default void initializeModule(BalmModule module) {
        final var modId = module.getId().getNamespace();
        module.registerConfig(getConfig());

        resourceConditions(modId, module::registerResourceConditions);
        dataComponentTypes(modId, module::registerDataComponentTypes);
        blocks(modId, module::registerBlocks);
        blockEntityTypes(modId, module::registerBlockEntityTypes);
        items(modId, module::registerItems);
        creativeModeTabs(modId, module::registerCreativeModeTabs);
        entityTypes(modId, module::registerEntityTypes);
        module.registerWorldGen(getWorldGen());
        module.registerNetworking(getNetworking());
        menuTypes(modId, module::registerMenuTypes);
        module.registerCapabilities(getCapabilities());
        module.registerCommands(getCommands());
        recipeTypes(modId, module::registerRecipeTypes);
        module.registerLootTables(getLootTables());
        customStats(modId, module::registerCustomStats);
        module.registerSoundEvents(registrar().scoped(Registries.SOUND_EVENT, modId));
        module.registerPermissions(getPermissions());
        particleTypes(modId, module::registerParticleTypes);
        module.registerAdditional(registrar());

        resourceReloadListeners(modId, module::registerReloadListeners);

        module.registerEvents(getEvents());
        module.initialize();
    }

    BalmProxy getProxy();

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmRegistrars registrars, BalmModule module);

    BalmRegistrar registrar();

    default <T> BalmRegistrar.Scoped<T> registrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return registrar().scoped(registryKey, namespace);
    }

    void blocks(String namespace, Consumer<BalmBlockRegistrar> initializer);

    void items(String namespace, Consumer<BalmItemRegistrar> initializer);

    void recipeTypes(String namespace, Consumer<BalmRecipeTypeRegistrar> initializer);

    void dataComponentTypes(String namespace, Consumer<BalmDataComponentTypeRegistrar> initializer);

    void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer);

    void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer);

    BalmPlatform platform();

    void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer);

    void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer);

    <TEvent> BidirectionalEventMapper<Consumer<TEvent>> createBoundCustomEvent(Class<TEvent> eventClass);
}
