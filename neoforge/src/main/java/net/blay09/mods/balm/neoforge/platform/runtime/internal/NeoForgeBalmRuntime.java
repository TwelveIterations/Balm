package net.blay09.mods.balm.neoforge.platform.runtime.internal;

import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.core.internal.DeferredRegisters;
import net.blay09.mods.balm.neoforge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.neoforge.platform.internal.NeoForgeBalmHooks;
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.platform.BalmHooks;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.neoforge.platform.capabilities.internal.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.commands.internal.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.platform.compatibility.internal.NeoForgeBalmModSupport;
import net.blay09.mods.balm.neoforge.platform.config.internal.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.core.internal.NeoForgeBalmRegistrar;
import net.blay09.mods.balm.neoforge.core.particles.internal.NeoForgeBalmParticleTypeRegistrar;
import net.blay09.mods.balm.neoforge.platform.event.internal.NeoForgeBalmEventMappings;
import net.blay09.mods.balm.neoforge.platform.internal.NeoForgeBalmPlatform;
import net.blay09.mods.balm.neoforge.network.internal.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.platform.permissions.internal.NeoForgeBalmPermissions;
import net.blay09.mods.balm.neoforge.server.packs.resources.internal.NeoForgeBalmResourceConditionRegistrar;
import net.blay09.mods.balm.neoforge.server.packs.resources.internal.NeoForgeBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.neoforge.stats.internal.NeoForgeBalmCustomStatRegistrar;
import net.blay09.mods.balm.neoforge.world.entity.npc.villager.internal.NeoForgeBalmVillagerTradeRegistrar;
import net.blay09.mods.balm.neoforge.world.level.levelgen.NeoForgeBalmWorldGen;
import net.blay09.mods.balm.neoforge.world.entity.internal.NeoForgeBalmEntityTypeRegistrar;
import net.blay09.mods.balm.neoforge.world.inventory.internal.NeoForgeBalmMenuTypeRegistrar;
import net.blay09.mods.balm.neoforge.world.item.internal.NeoForgeBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.neoforge.world.level.block.entity.internal.NeoForgeBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.config.BalmConfig;
import net.blay09.mods.balm.platform.permissions.BalmPermissions;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.platform.runtime.internal.CommonBalmRuntime;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.entity.npc.villager.BalmVillagerTradeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.neoforge.platform.attachment.internal.NeoForgeBalmDataAttachmentTypeRegistrar;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.function.Consumer;

public class NeoForgeBalmRuntime extends CommonBalmRuntime<NeoForgeLoadContext> {

    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
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
        NeoForgeBalmEventMappings.bind();
    }

    @Override
    public BalmConfig getConfig() {
        return config;
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
    public void initializeMod(String modId, NeoForgeLoadContext context, Consumer<BalmRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmRegistrars(this, modId));

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
    public void villagerTrades(String namespace, Consumer<BalmVillagerTradeRegistrar> initializer) {
        NeoForge.EVENT_BUS.addListener((VillagerTradesEvent event) -> initializer.accept(new NeoForgeBalmVillagerTradeRegistrar(event)));
    }

    @Override
    public BalmPlatform platform() {
        return platform;
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

    @Override
    public void dataAttachmentTypes(String namespace, Consumer<BalmDataAttachmentTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmDataAttachmentTypeRegistrar(registrar(), namespace));
    }

}