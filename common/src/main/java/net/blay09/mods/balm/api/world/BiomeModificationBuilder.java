package net.blay09.mods.balm.api.world;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface BiomeModificationBuilder {
    void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> placedFeature);

    RegistryAccess registryAccess();

    default Registry<PlacedFeature> placedFeatures() {
        return registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
    }
}
