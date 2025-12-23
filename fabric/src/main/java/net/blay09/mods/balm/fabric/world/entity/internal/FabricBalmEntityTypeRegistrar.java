package net.blay09.mods.balm.fabric.world.entity.internal;

import net.blay09.mods.balm.world.entity.internal.AbstractBalmEntityTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FabricBalmEntityTypeRegistrar extends AbstractBalmEntityTypeRegistrarImpl {
    public FabricBalmEntityTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void registerDefaultAttributes(Holder<EntityType<T>> entityTypeHolder, Supplier<AttributeSupplier.Builder> attributes) {
        final var entityType = entityTypeHolder.value();
        FabricDefaultAttributeRegistry.register((EntityType<? extends @NotNull LivingEntity>) entityType, attributes.get());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void registerSpawnPlacement(Holder<EntityType<T>> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, Supplier<SpawnPlacements.SpawnPredicate<T>> supplier) {
        SpawnPlacements.register((EntityType<Mob>) entityType.value(), spawnPlacementType, heightmapType, (SpawnPlacements.SpawnPredicate<Mob>) supplier.get());
    }
}
