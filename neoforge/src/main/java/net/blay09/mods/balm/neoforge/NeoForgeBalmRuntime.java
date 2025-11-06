package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.neoforge.server.packs.resources.NeoForgeBalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.command.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.compat.NeoForgeBalmModSupport;
import net.blay09.mods.balm.neoforge.config.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.world.entity.NeoForgeBalmEntityTypeRegistrar;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmCommonEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.world.level.block.entity.NeoForgeBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.neoforge.loader.NeoForgeBalmPlatform;
import net.blay09.mods.balm.neoforge.world.inventory.NeoForgeBalmMenuTypeRegistrar;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.core.particles.NeoForgeBalmParticleTypeRegistrar;
import net.blay09.mods.balm.neoforge.permission.NeoForgeBalmPermissions;
import net.blay09.mods.balm.neoforge.core.NeoForgeBalmRegistrar;
import net.blay09.mods.balm.neoforge.stats.NeoForgeBalmCustomStatRegistrar;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.blay09.mods.balm.neoforge.world.item.NeoForgeBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.neoforge.server.packs.resources.NeoForgeBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.function.Consumer;

public class NeoForgeBalmRuntime extends CommonBalmRuntime<NeoForgeLoadContext> {

    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
    private final NeoForgeBalmEvents events = new NeoForgeBalmEvents();
    private final BalmNetworking networking = new NeoForgeBalmNetworking();
    private final BalmConfig config = new NeoForgeBalmConfig();
    private final BalmHooks hooks = new NeoForgeBalmHooks();
    private final BalmCapabilities capabilities = new NeoForgeBalmCapabilities();
    private final BalmCommands commands = new NeoForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmModSupport modSupport = new NeoForgeBalmModSupport(this);
    private final BalmPermissions permissions = new NeoForgeBalmPermissions();
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
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmCreativeModeTabRegistrar(registrar(), namespace));
    }

    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmBlockEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmMenuTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmParticleTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmCustomStatRegistrar(registrar(), namespace));
    }

    @Override
    public BalmCreativeModeTabRegistrar creativeModeTabs(String namespace) {
        return new NeoForgeBalmCreativeModeTabRegistrar(registrar(), namespace);
    }

    @Override
    public BalmBlockEntityTypeRegistrar blockEntityTypes(String namespace) {
        return new NeoForgeBalmBlockEntityTypeRegistrar(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmMenuTypeRegistrar menuTypes(String namespace) {
        return new NeoForgeBalmMenuTypeRegistrar(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmEntityTypeRegistrar entityTypes(String namespace) {
        return new NeoForgeBalmEntityTypeRegistrar(registrar(), namespace);
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    public BalmParticleTypeRegistrar particleTypes(String namespace) {
        return new NeoForgeBalmParticleTypeRegistrar(registrar(), namespace);
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        NeoForge.EVENT_BUS.addListener((AddServerReloadListenersEvent event) ->
                initializer.accept(new NeoForgeBalmResourceReloadListenerRegistrar(namespace, event)));
    }

    @Override
    public void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmResourceConditionRegistrar(namespace));
    }
}