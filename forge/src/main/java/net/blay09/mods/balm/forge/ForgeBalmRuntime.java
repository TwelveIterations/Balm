package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.common.CommonBalmRuntime;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.event.EventMapper;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.compat.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.core.ForgeBalmRegistrar;
import net.blay09.mods.balm.forge.core.particles.ForgeBalmParticleTypeRegistrar;
import net.blay09.mods.balm.forge.event.*;
import net.blay09.mods.balm.forge.loader.ForgeBalmPlatform;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.permission.ForgeBalmPermissions;
import net.blay09.mods.balm.forge.server.packs.resources.ForgeBalmResourceConditionRegistrar;
import net.blay09.mods.balm.forge.server.packs.resources.ForgeBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.forge.stats.ForgeBalmCustomStatRegistrar;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.blay09.mods.balm.forge.world.block.entity.ForgeBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.forge.world.entity.ForgeBalmEntityTypeRegistrar;
import net.blay09.mods.balm.forge.world.inventory.ForgeBalmMenuTypeRegistrar;
import net.blay09.mods.balm.forge.world.item.ForgeBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.bus.EventBus;

import java.util.function.Consumer;

public class ForgeBalmRuntime extends CommonBalmRuntime<ForgeLoadContext> {

    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmCapabilities capabilities = new ForgeBalmCapabilities();
    private final BalmCommands commands = new ForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmModSupport modSupport = new ForgeBalmModSupport(this);
    private final BalmPermissions permissions = new ForgeBalmPermissions();
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
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public void initializeMod(String modId, ForgeLoadContext context, Consumer<BalmRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmRegistrars(this));

        final var modEventBus = context.modBusGroup();
        DeferredRegisters.register(modId, modEventBus);
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
    }

    @Override
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmBlockEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer) {
        initializer.accept(new ForgeBalmCreativeModeTabRegistrar(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmMenuTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmParticleTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer) {
        initializer.accept(new ForgeBalmCustomStatRegistrar(registrar(), namespace));
    }

    @Override
    @Deprecated
    public BalmCreativeModeTabRegistrar creativeModeTabs(String namespace) {
        return new ForgeBalmCreativeModeTabRegistrar(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmBlockEntityTypeRegistrar blockEntityTypes(String namespace) {
        return new ForgeBalmBlockEntityTypeRegistrar(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmEntityTypeRegistrar entityTypes(String namespace) {
        return new ForgeBalmEntityTypeRegistrar(registrar(), namespace);
    }

    @Override
    @Deprecated
    public BalmMenuTypeRegistrar menuTypes(String namespace) {
        return new ForgeBalmMenuTypeRegistrar(registrar(), namespace);
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    public BalmParticleTypeRegistrar particleTypes(String namespace) {
        return new ForgeBalmParticleTypeRegistrar(registrar(), namespace);
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        AddReloadListenerEvent.BUS.addListener((AddReloadListenerEvent event) -> initializer.accept(new ForgeBalmResourceReloadListenerRegistrar(event)));
    }

    @Override
    public void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer) {
        initializer.accept(new ForgeBalmResourceConditionRegistrar(namespace));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TEvent> EventMapper<Consumer<TEvent>> createBoundCustomEvent(Class<TEvent> eventClass) {
        final var bus = EventBus.create(ForgifiedEvent.class);
        final var mapper = new ForgeCustomEventMapper<TEvent>(bus);
        mapper.setup(
                (phase, listener) -> bus.addListener(ForgeBalmEventMappings.mapPriority(phase), event -> listener.accept((TEvent) event.data())),
                () -> (event) -> bus.fire(new ForgifiedEvent<>(event)));
        return mapper;
    }
}