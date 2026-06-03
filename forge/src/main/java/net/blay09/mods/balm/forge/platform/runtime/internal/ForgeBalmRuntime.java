package net.blay09.mods.balm.forge.platform.runtime.internal;

import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.commands.BalmArgumentTypeRegistrar;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.forge.platform.attachment.internal.ForgeBalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.forge.commands.internal.ForgeBalmArgumentTypeRegistrar;
import net.blay09.mods.balm.forge.platform.runtime.ForgeLoadContext;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.forge.core.internal.DeferredRegisters;
import net.blay09.mods.balm.forge.core.internal.ForgeRegistryAliasRemapper;
import net.blay09.mods.balm.forge.platform.capabilities.internal.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.commands.internal.ForgeBalmCommands;
import net.blay09.mods.balm.forge.platform.compatibility.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.platform.config.internal.ForgeBalmConfig;
import net.blay09.mods.balm.forge.core.internal.ForgeBalmRegistrar;
import net.blay09.mods.balm.forge.core.particles.internal.ForgeBalmParticleTypeRegistrar;
import net.blay09.mods.balm.forge.platform.internal.ForgeBalmHooks;
import net.blay09.mods.balm.forge.platform.internal.ForgeBalmPlatform;
import net.blay09.mods.balm.forge.network.internal.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.platform.permissions.internal.ForgeBalmPermissions;
import net.blay09.mods.balm.forge.platform.event.internal.ForgeBalmEventMappings;
import net.blay09.mods.balm.forge.server.packs.resources.internal.ForgeBalmResourceConditionRegistrar;
import net.blay09.mods.balm.forge.server.packs.resources.internal.ForgeBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.forge.stats.internal.ForgeBalmCustomStatRegistrar;
import net.blay09.mods.balm.forge.world.level.levelgen.internal.ForgeBalmWorldGen;
import net.blay09.mods.balm.forge.world.block.entity.internal.ForgeBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.forge.world.entity.internal.ForgeBalmEntityTypeRegistrar;
import net.blay09.mods.balm.forge.world.inventory.internal.ForgeBalmMenuTypeRegistrar;
import net.blay09.mods.balm.forge.world.item.internal.ForgeBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.forge.world.item.internal.ForgeBalmCompostableRegistrar;
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
import net.blay09.mods.balm.world.item.BalmCompostableRegistrar;
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
        ModBusEventRegisters.getRegistrations(modId, ForgeRegistryAliasRemapper.class);
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
        initializer.accept(new ForgeBalmDataAttachmentTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void compostables(String namespace, Consumer<BalmCompostableRegistrar> initializer) {
        initializer.accept(new ForgeBalmCompostableRegistrar());
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
    public void argumentTypes(String namespace, Consumer<BalmArgumentTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmArgumentTypeRegistrar(namespace));
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
