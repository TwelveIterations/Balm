package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.fabric.core.particles.FabricBalmParticleTypeRegistrar;
import net.blay09.mods.balm.fabric.server.packs.resources.FabricBalmResourceConditionRegistrar;
import net.blay09.mods.balm.fabric.world.entity.FabricBalmEntityTypeRegistrar;
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
import net.blay09.mods.balm.common.permission.CommonBalmPermissions;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.fabric.capability.FabricBalmCapabilities;
import net.blay09.mods.balm.fabric.command.FabricBalmCommands;
import net.blay09.mods.balm.fabric.compat.FabricBalmModSupport;
import net.blay09.mods.balm.fabric.config.FabricBalmConfig;
import net.blay09.mods.balm.fabric.event.FabricBalmCommonEvents;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.world.level.block.entity.FabricBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.fabric.loader.FabricBalmPlatform;
import net.blay09.mods.balm.fabric.world.inventory.FabricBalmMenuTypeRegistrar;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.core.FabricBalmRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.fabric.stats.FabricBalmCustomStatRegistrar;
import net.blay09.mods.balm.fabric.world.FabricBalmWorldGen;
import net.blay09.mods.balm.fabric.world.item.FabricBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.fabric.server.packs.resources.FabricBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricBalmRuntime extends CommonBalmRuntime<EmptyLoadContext> {
    private final BalmWorldGen worldGen = new FabricBalmWorldGen();
    private final FabricBalmEvents events = new FabricBalmEvents();
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
            .with("fabric-permissions-api-v0", "net.blay09.mods.balm.fabric.compat.FabricPermissionsAPIIntegration")
            .withFallback(new CommonBalmPermissions())
            .buildLazily();

    public FabricBalmRuntime() {
        FabricBalmCommonEvents.registerEvents(events);
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
    public void initializeMod(String modId, EmptyLoadContext context, Consumer<BalmRegistrars> initializer) {
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
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmBlockEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmMenuTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, java.util.function.Consumer<BalmEntityTypeRegistrar> initializer) {
        initializer.accept(new FabricBalmEntityTypeRegistrar(registrar(), namespace));
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
}
