package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
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
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.platform.BalmHooks;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
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
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.function.Consumer;

public class ForgeBalmRuntime extends CommonBalmRuntime<ForgeLoadContext> {

    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
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
        ForgeBalmEventMappings.bind();
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
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public void initializeMod(String modId, ForgeLoadContext context, Consumer<BalmRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmRegistrars(this, modId));

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
    public void dataAttachmentTypes(String namespace, Consumer<BalmDataAttachmentTypeRegistrar> initializer) {
        throw new UnsupportedOperationException("Data Attachments are not currently supported on Forge.");
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
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        AddReloadListenerEvent.BUS.addListener((AddReloadListenerEvent event) -> initializer.accept(new ForgeBalmResourceReloadListenerRegistrar(event)));
    }

    @Override
    public void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer) {
        initializer.accept(new ForgeBalmResourceConditionRegistrar(namespace));
    }

}