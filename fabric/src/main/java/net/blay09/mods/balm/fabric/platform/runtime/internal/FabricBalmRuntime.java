package net.blay09.mods.balm.fabric.platform.runtime.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.commands.BalmArgumentTypeRegistrar;
import net.blay09.mods.balm.fabric.commands.internal.FabricBalmArgumentTypeRegistrar;
import net.blay09.mods.balm.fabric.commands.internal.FabricBalmCommands;
import net.blay09.mods.balm.fabric.core.internal.FabricBalmRegistrar;
import net.blay09.mods.balm.fabric.core.particles.internal.FabricBalmParticleTypeRegistrar;
import net.blay09.mods.balm.fabric.network.internal.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.platform.capabilities.internal.FabricBalmCapabilities;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmEventMappings;
import net.blay09.mods.balm.fabric.platform.internal.FabricBalmHooks;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.balm.fabric.stats.internal.FabricBalmCustomStatRegistrar;
import net.blay09.mods.balm.fabric.world.level.levelgen.internal.FabricBalmWorldGen;
import net.blay09.mods.balm.platform.BalmHooks;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.config.BalmConfig;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.fabric.server.packs.resources.internal.FabricBalmResourceConditionRegistrar;
import net.blay09.mods.balm.fabric.world.entity.internal.FabricBalmEntityTypeRegistrar;
import net.blay09.mods.balm.fabric.world.entity.ai.village.poi.internal.FabricBalmPoiTypeRegistrar;
import net.blay09.mods.balm.platform.runtime.internal.CommonBalmRuntime;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.platform.permissions.BalmPermissions;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.blay09.mods.balm.platform.permissions.internal.CommonBalmPermissions;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.fabric.platform.compatibility.internal.FabricBalmModSupport;
import net.blay09.mods.balm.fabric.platform.config.internal.FabricBalmConfig;
import net.blay09.mods.balm.fabric.world.level.block.entity.internal.FabricBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.fabric.platform.internal.FabricBalmPlatform;
import net.blay09.mods.balm.fabric.world.inventory.internal.FabricBalmMenuTypeRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.fabric.world.item.internal.FabricBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.fabric.world.item.internal.FabricBalmCompostableRegistrar;
import net.blay09.mods.balm.fabric.server.packs.resources.internal.FabricBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmCompostableRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.world.level.storage.loot.internal.CommonBalmLootTables;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.fabric.platform.attachment.internal.FabricBalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.world.entity.ai.village.poi.BalmPoiTypeRegistrar;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricBalmRuntime extends CommonBalmRuntime<FabricLoadContext> {
    private final BalmWorldGen worldGen = new FabricBalmWorldGen();
    private final BalmNetworking networking = new FabricBalmNetworking();
    private final BalmConfig config = new FabricBalmConfig();
    private final BalmHooks hooks = new FabricBalmHooks();
    private final BalmRegistrar registrar = new FabricBalmRegistrar();
    private final BalmCapabilities capabilities = new FabricBalmCapabilities();
    private final BalmCommands commands = new FabricBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmModSupport modSupport = new FabricBalmModSupport(this);
    private final BalmPlatform platform = new FabricBalmPlatform();
    private final Supplier<BalmPermissions> permissions = this.<BalmPermissions>modProxy()
            .with("fabric-permissions-api-v0", "net.blay09.mods.balm.fabric.platform.compatibility.permissions.internal.FabricPermissionsAPIIntegration")
            .withFallback(new CommonBalmPermissions())
            .buildLazily();

    public FabricBalmRuntime() {
        FabricBalmSupplementalEvents.initialize();
        FabricBalmEventMappings.bind();
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
    public void initializeMod(String modId, FabricLoadContext context, Consumer<BalmRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmRegistrars(this, modId));
    }

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
    }

    @Override
    public BalmPermissions getPermissions() {
        return permissions.get();
    }

    @Override
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer) {
        initializer.accept(new FabricBalmCreativeModeTabRegistrar(registrar(), namespace));
    }

    @Override
    public void compostables(String namespace, Consumer<BalmCompostableRegistrar> initializer) {
        initializer.accept(new FabricBalmCompostableRegistrar());
    }

    @Override
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmBlockEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmMenuTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void poiTypes(String namespace, Consumer<BalmPoiTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmPoiTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmParticleTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer) {
        initializer.accept(new FabricBalmCustomStatRegistrar(registrar(), namespace));
    }

    @Override
    public void argumentTypes(String namespace, Consumer<BalmArgumentTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmArgumentTypeRegistrar(namespace));
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        initializer.accept(new FabricBalmResourceReloadListenerRegistrar(namespace));
    }

    @Override
    public void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer) {
        initializer.accept(new FabricBalmResourceConditionRegistrar(namespace));
    }

    @Override
    public void dataAttachmentTypes(String namespace, Consumer<BalmDataAttachmentTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmDataAttachmentTypeRegistrar(namespace));
    }

}
