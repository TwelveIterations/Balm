package net.blay09.mods.balm.forge;

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
import net.blay09.mods.balm.api.stats.BalmCustomStatFactory;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.compat.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.core.ForgeBalmRegistrar;
import net.blay09.mods.balm.forge.entity.ForgeBalmEntityTypeFactory;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.level.block.entity.ForgeBalmBlockEntityTypeFactory;
import net.blay09.mods.balm.forge.loader.ForgeBalmPlatform;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenuTypeFactory;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.particle.ForgeBalmParticleTypeFactory;
import net.blay09.mods.balm.forge.permission.ForgeBalmPermissions;
import net.blay09.mods.balm.forge.recipe.ForgeBalmRecipes;
import net.blay09.mods.balm.forge.resources.ForgeBalmResources;
import net.blay09.mods.balm.forge.stats.ForgeBalmCustomStatFactory;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.blay09.mods.balm.forge.world.item.ForgeBalmCreativeModeTabFactory;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeFactory;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.function.Consumer;

public class ForgeBalmRuntime extends CommonBalmRuntime<ForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmCapabilities capabilities = new ForgeBalmCapabilities(legacyNamespaceResolver);
    private final BalmCommands commands = new ForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmRecipes recipes = new ForgeBalmRecipes();
    private final BalmModSupport modSupport = new ForgeBalmModSupport(this);
    private final BalmPermissions permissions = new ForgeBalmPermissions();
    private final BalmResources resources = new ForgeBalmResources();
    private final BalmRegistrar registrar = new ForgeBalmRegistrar();
    private final BalmPlatform platform = new ForgeBalmPlatform();

    public ForgeBalmRuntime() {
        ForgeBalmCommonEvents.registerEvents(events);
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
    public BalmRecipes getRecipes() {
        return recipes;
    }

    @Override
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public void initializeMod(String modId, ForgeLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();

        final var modEventBus = context.modBusGroup();
        DeferredRegisters.register(modId, modEventBus);
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
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
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeFactory> initializer) {
        initializer.accept(new ForgeBalmBlockEntityTypeFactory(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeFactory> initializer) {
        initializer.accept(new ForgeBalmEntityTypeFactory(registrar(), namespace));
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabFactory> initializer) {
        initializer.accept(new ForgeBalmCreativeModeTabFactory(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeFactory> initializer) {
        initializer.accept(new ForgeBalmMenuTypeFactory(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeFactory> initializer) {
        initializer.accept(new ForgeBalmParticleTypeFactory(registrar(), namespace));
    }

    @Override
    public void customStats(String namespace, Consumer<BalmCustomStatFactory> initializer) {
        initializer.accept(new ForgeBalmCustomStatFactory(registrar(), namespace));
    }

    @Override
    @Deprecated
    public BalmCreativeModeTabFactory creativeModeTabs(String namespace) {
        return new ForgeBalmCreativeModeTabFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmBlockEntityTypeFactory blockEntityTypes(String namespace) {
        return new ForgeBalmBlockEntityTypeFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmEntityTypeFactory entityTypes(String namespace) {
        return new ForgeBalmEntityTypeFactory(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmMenuTypeFactory menuTypes(String namespace) {
        return new ForgeBalmMenuTypeFactory(registrar(), namespace);
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    public BalmParticleTypeFactory particleTypes(String namespace) {
        return new ForgeBalmParticleTypeFactory(registrar(), namespace);
    }

    @Override
    public BalmCustomStatFactory customStats(String namespace) {
        return new ForgeBalmCustomStatFactory(registrar(), namespace);
    }
}