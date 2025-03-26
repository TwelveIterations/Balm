package net.blay09.mods.balm.api.module;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
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
import net.minecraft.resources.ResourceLocation;

public interface BalmModule {
    ResourceLocation getId();

    default void registerStats(BalmStats stats) {
    }

    default void registerSounds(BalmSounds sounds) {
    }

    default void registerParticles(BalmParticles particles) {
    }

    default void registerMenus(BalmMenus menus) {
    }

    default void registerRecipes(BalmRecipes recipes) {
    }

    default void registerCommands(BalmCommands commands) {
    }

    default void registerEntities(BalmEntities entities) {
    }

    default void registerLootTables(BalmLootTables lootTables) {
    }

    default void registerItems(BalmItems items) {
    }

    default void registerBlockEntities(BalmBlockEntities blockEntities) {
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

    default void registerBlocks(BalmBlocks blocks) {
    }

    default void registerEvents(BalmEvents events) {
    }

    default void registerComponents(BalmComponents components) {
    }

    default void initialize() {
    }

    default void registerResources(BalmResources resources) {
    }
}
