package net.blay09.mods.balm.forge.world.level.biome.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.world.level.biome.BiomeModificationBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ForgeBiomeModificationBuilder(
        ModifiableBiomeInfo.BiomeInfo.Builder builder) implements BiomeModificationBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ForgeBiomeModificationBuilder.class);

    @Override
    public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeature) {
        final var server = Balm.platform().server();
        if (server != null) {
            final var placedFeatures = server.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
            builder.getGenerationSettings().addFeature(step, placedFeatures.getOrThrow(placedFeature));
        } else {
            logger.error("Failed to add feature {} to biome, no server is available", placedFeature);
        }
    }

    @Override
    public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry, int weight) {
        builder.getMobSpawnSettings().addSpawn(spawnGroup, weight, spawnEntry);
    }

    @Override
    public void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
        builder.getMobSpawnSettings().addMobCharge(entityType, mass, gravityLimit);
    }
}
