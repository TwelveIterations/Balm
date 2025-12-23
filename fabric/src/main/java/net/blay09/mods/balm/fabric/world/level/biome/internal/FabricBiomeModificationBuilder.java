package net.blay09.mods.balm.fabric.world.level.biome.internal;

import net.blay09.mods.balm.world.level.biome.BiomeModificationBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record FabricBiomeModificationBuilder(BiomeModificationContext builder) implements BiomeModificationBuilder {
    @Override
    public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeature) {
        builder.getGenerationSettings().addFeature(step, placedFeature);
    }

    @Override
    public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry) {
        builder.getSpawnSettings().addSpawn(spawnGroup, spawnEntry);
    }

    @Override
    public void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
        builder.getSpawnSettings().setSpawnCost(entityType, mass, gravityLimit);
    }
}
