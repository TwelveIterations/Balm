package net.blay09.mods.balm.api.module;

import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmArgumentTypeRegistrar;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.entity.ai.village.poi.BalmPoiTypeRegistrar;
import net.blay09.mods.balm.world.entity.npc.villager.BalmVillagerTradeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@SuppressWarnings({"DeprecatedIsStillUsed"})
public interface BalmModule {
    ResourceLocation getId();

    /**
     * @deprecated Use {@link #registerCustomStats(BalmCustomStatRegistrar)} instead.
     */
    @Deprecated
    default void registerStats(BalmStats stats) {
    }

    /**
     * @deprecated {@link #registerSoundEvents(BalmRegistrar.Scoped)} instead.
     */
    @Deprecated
    default void registerSounds(BalmSounds sounds) {
    }

    /**
     * @deprecated Use {@link #registerParticleTypes(BalmParticleTypeRegistrar)} instead.
     */
    @Deprecated
    default void registerParticles(BalmParticles particles) {
    }

    /**
     * @deprecated Use {@link #registerMenuTypes(BalmMenuTypeRegistrar)} instead.
     */
    @Deprecated
    default void registerMenus(BalmMenus menus) {
    }

    /**
     * @deprecated Use {@link #registerRecipeTypes(BalmRecipeTypeRegistrar)} instead.
     */
    @Deprecated
    default void registerRecipes(BalmRecipes recipes) {
    }

    default void registerCommands(BalmCommands commands) {
    }

    default void registerArgumentTypes(BalmArgumentTypeRegistrar argumentTypes) {
    }

    /**
     * @deprecated Use {@link #registerEntityTypes(BalmEntityTypeRegistrar)} instead.
     */
    @Deprecated
    default void registerEntities(BalmEntities entities) {
    }

    default void registerLootTables(BalmLootTables lootTables) {
    }

    /**
     * @deprecated Use {@link #registerItems(BalmItemRegistrar)} and {@link #registerCreativeModeTabs(BalmCreativeModeTabRegistrar)} instead.
     */
    @Deprecated
    default void registerItems(BalmItems items) {
    }

    /**
     * @deprecated Use {@link #registerBlockEntityTypes(BalmBlockEntityTypeRegistrar)} instead.
     */
    @Deprecated
    default void registerBlockEntities(BalmBlockEntities blockEntities) {
    }

    default void registerVillagerTrades(BalmVillagerTradeRegistrar villagerTrades) {
    }

    default void registerWorldGen(BalmWorldGen worldGen) {
    }

    default void registerPoiTypes(BalmPoiTypeRegistrar poiTypes) {
    }

    default void registerNetworking(BalmNetworking networking) {
    }

    default void registerCapabilities(BalmCapabilities capabilities) {
    }

    default void registerPermissions(BalmPermissions permissions) {
    }

    default void registerConfig(BalmConfig config) {
    }

    /**
     * @deprecated Use {@link #registerBlocks(BalmBlockRegistrar)} instead.
     */
    @Deprecated
    default void registerBlocks(BalmBlocks blocks) {
    }

    default void registerEvents(BalmEvents events) {
    }

    /**
     * @deprecated Use {@link #registerDataComponentTypes(BalmDataComponentTypeRegistrar)} instead.
     */
    @Deprecated
    default void registerComponents(BalmComponents components) {
    }

    default void registerAdditional(BalmRegistries registries) {
    }

    default void initialize() {
    }

    /**
     * @deprecated Use {@link #registerResourceConditions(BalmResourceConditionRegistrar)} instead.
     */
    @Deprecated
    default void registerResources(BalmResources resources) {
    }

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

    default void registerEntityTypes(BalmEntityTypeRegistrar entityTypes) {
    }

    default void registerItems(BalmItemRegistrar items) {
    }

    default void registerCreativeModeTabs(BalmCreativeModeTabRegistrar creativeModeTabs) {
    }

    default void registerBlockEntityTypes(BalmBlockEntityTypeRegistrar blockEntityTypes) {
    }

    default void registerBlocks(BalmBlockRegistrar factory) {
    }

    default void registerDataComponentTypes(BalmDataComponentTypeRegistrar dataComponentTypes) {
    }

    default void registerDataAttachmentTypes(BalmDataAttachmentTypeRegistrar dataAttachmentTypes) {
    }

    default void registerAdditional(BalmRegistrar registrar) {
    }

    default void registerResourceConditions(BalmResourceConditionRegistrar resourceConditions) {
    }

    default void registerReloadListeners(BalmResourceReloadListenerRegistrar resourceReloadListeners) {
    }
}
