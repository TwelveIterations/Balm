package net.blay09.mods.balm.forge.world;

import net.blay09.mods.balm.api.world.BiomeModificationBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record ForgeBiomeModificationBuilder(RegistryAccess registryAccess,
                                     ModifiableBiomeInfo.BiomeInfo.Builder builder) implements BiomeModificationBuilder {
    @Override
    public void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> placedFeature) {
        builder.getGenerationSettings().addFeature(step, placedFeature);
    }
}
