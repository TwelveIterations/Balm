package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntityTypeFactory;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenuTypeFactory;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticleTypeFactory;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.command.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.compat.NeoForgeBalmModSupport;
import net.blay09.mods.balm.neoforge.config.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.entity.NeoForgeBalmEntityTypeFactory;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmCommonEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.level.block.entity.NeoForgeBalmBlockEntityTypeFactory;
import net.blay09.mods.balm.neoforge.loader.NeoForgeBalmPlatform;
import net.blay09.mods.balm.neoforge.menu.NeoForgeBalmMenuTypeFactory;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.particle.NeoForgeBalmParticleTypeFactory;
import net.blay09.mods.balm.neoforge.permission.NeoForgeBalmPermissions;
import net.blay09.mods.balm.neoforge.recipe.NeoForgeBalmRecipes;
import net.blay09.mods.balm.neoforge.core.NeoForgeBalmRegistrar;
import net.blay09.mods.balm.neoforge.resources.NeoForgeBalmResources;
import net.blay09.mods.balm.neoforge.stats.NeoForgeBalmStats;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.blay09.mods.balm.neoforge.world.item.NeoForgeBalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeFactory;
import net.neoforged.fml.ModLoadingContext;

import java.util.function.Consumer;

public class NeoForgeBalmRuntime extends CommonBalmRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
    private final NeoForgeBalmEvents events = new NeoForgeBalmEvents();
    private final BalmNetworking networking = new NeoForgeBalmNetworking(legacyNamespaceResolver);
    private final BalmConfig config = new NeoForgeBalmConfig();
    private final BalmHooks hooks = new NeoForgeBalmHooks();
    private final BalmCapabilities capabilities = new NeoForgeBalmCapabilities(legacyNamespaceResolver);
    private final BalmCommands commands = new NeoForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new NeoForgeBalmStats(legacyNamespaceResolver);
    private final BalmRecipes recipes = new NeoForgeBalmRecipes();
    private final BalmModSupport modSupport = new NeoForgeBalmModSupport(this);
    private final BalmPermissions permissions = new NeoForgeBalmPermissions();
    private final BalmResources resources = new NeoForgeBalmResources();
    private final BalmRegistrar registrar = new NeoForgeBalmRegistrar();
    private final BalmPlatform platform = new NeoForgeBalmPlatform();

    public NeoForgeBalmRuntime() {
        NeoForgeBalmCommonEvents.registerEvents(events);
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
    public void initializeMod(String modId, NeoForgeLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();

        final var modBus = context.modBus();
        DeferredRegisters.register(modId, modBus);
        ModBusEventRegisters.register(modId, modBus);
    }

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
    }

    @Override
    public BalmPermissions getPermissions() {
        return permissions;
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
        initializer.accept(new NeoForgeBalmCreativeModeTabFactory(registrar(), namespace));
    }

    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeFactory> initializer) {
        initializer.accept(new NeoForgeBalmBlockEntityTypeFactory(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeFactory> initializer) {
        initializer.accept(new NeoForgeBalmEntityTypeFactory(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeFactory> initializer) {
        initializer.accept(new NeoForgeBalmMenuTypeFactory(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeFactory> initializer) {
        initializer.accept(new NeoForgeBalmParticleTypeFactory(registrar(), namespace));
    }

    @Override
    public BalmCreativeModeTabFactory creativeModeTabs(String namespace) {
        return new NeoForgeBalmCreativeModeTabFactory(registrar(), namespace);
    }

    @Override
    public BalmBlockEntityTypeFactory blockEntityTypes(String namespace) {
        return new NeoForgeBalmBlockEntityTypeFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmMenuTypeFactory menuTypes(String namespace) {
        return new NeoForgeBalmMenuTypeFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmEntityTypeFactory entityTypes(String namespace) {
        return new NeoForgeBalmEntityTypeFactory(registrar(), namespace);
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    public BalmParticleTypeFactory particleTypes(String namespace) {
        return new NeoForgeBalmParticleTypeFactory(registrar(), namespace);
    }
}