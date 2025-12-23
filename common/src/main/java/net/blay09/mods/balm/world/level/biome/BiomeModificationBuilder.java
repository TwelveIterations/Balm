package net.blay09.mods.balm.world.level.biome;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface BiomeModificationBuilder {
    void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeature);

    void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry, int weight);

    void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit);
}
