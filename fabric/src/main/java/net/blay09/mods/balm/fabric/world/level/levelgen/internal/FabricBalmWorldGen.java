package net.blay09.mods.balm.fabric.world.level.levelgen.internal;

import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.blay09.mods.balm.world.level.biome.BiomeModificationBuilder;
import net.blay09.mods.balm.world.level.biome.BiomeModifier;
import net.blay09.mods.balm.world.level.biome.BiomePredicate;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class FabricBalmWorldGen implements BalmWorldGen {

    public record FabricBiomeModificationBuilder(BiomeModificationContext builder) implements BiomeModificationBuilder {
        @Override
        public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeature) {
            builder.getGenerationSettings().addFeature(step, placedFeature);
        }

        @Override
        public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry, int weight) {
            builder.getMobSpawnSettings().addSpawn(spawnGroup, spawnEntry, weight);
        }

        @Override
        public void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
            builder.getMobSpawnSettings().addMobCharge(entityType, mass, gravityLimit);
        }
    }

    @Override
    public void modifyBiome(Identifier id, BiomePredicate predicate, BiomeModifier modifier) {
        BiomeModifications.create(id)
                .add(ModificationPhase.ADDITIONS,
                        it -> predicate.test(it.getBiomeHolder()),
                        (selectionContext, modificationContext) -> modifier.modifyBiome(selectionContext.getBiomeHolder(), new FabricBiomeModificationBuilder(modificationContext)));
    }
}
