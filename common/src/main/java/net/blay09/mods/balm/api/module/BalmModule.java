package net.blay09.mods.balm.api.module;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

/**
 * This interface provides an easy and structured way of interacting with Balm.
 * <p>
 * Once a module is registered using {@link net.blay09.mods.balm.api.Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)}, its <code>register{...}</code> methods will be called automatically.
 */
public interface BalmModule {
    /**
     * Should return a unique identifier for this module, e.g. <code>yourmod:common</code>. The namespace must be your mod id.
     * @return a unique identifier for this module.
     */
    Identifier getId();

    default void registerSoundEvents(BalmRegistrar.Scoped<SoundEvent> sounds) {
    }

    default void registerParticleTypes(BalmParticleTypeRegistrar particleTypes) {
    }

    default void registerCustomStats(BalmCustomStatRegistrar customStats) {
    }

    default void registerMenuTypes(BalmMenuTypeRegistrar menuTypes) {
    }

    default void registerRecipeTypes(BalmRecipeTypeRegistrar recipeTypes) {
    }

    default void registerCommands(BalmCommands commands) {
    }

    default void registerEntityTypes(BalmEntityTypeRegistrar entityTypes) {
    }

    default void registerLootTables(BalmLootTables lootTables) {
    }

    default void registerItems(BalmItemRegistrar items) {
    }

    default void registerCreativeModeTabs(BalmCreativeModeTabRegistrar creativeModeTabs) {
    }

    default void registerBlockEntityTypes(BalmBlockEntityTypeRegistrar blockEntityTypes) {
    }

    default void registerWorldGen(BalmWorldGen worldGen) {
    }

    default void registerNetworking(BalmNetworking networking) {
    }

    default void registerCapabilities(BalmCapabilities capabilities) {
    }

    default void registerPermissions(BalmPermissions permissions) {
    }

    default void registerConfig(BalmConfig config) {
    }

    default void registerBlocks(BalmBlockRegistrar factory) {
    }

    default void registerEvents(BalmEvents events) {
    }

    default void registerDataComponentTypes(BalmDataComponentTypeRegistrar dataComponentTypes) {
    }

    default void registerAdditional(BalmRegistrar registrar) {
    }

    default void initialize() {
    }

    default void registerResourceConditions(BalmResourceConditionRegistrar resourceConditions) {
    }

    default void registerReloadListeners(BalmResourceReloadListenerRegistrar resourceReloadListeners) {
    }
}
