package net.blay09.mods.balm.neoforge.world.entity.internal;

import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.blay09.mods.balm.world.entity.internal.AbstractBalmEntityTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeBalmEntityTypeRegistrar extends AbstractBalmEntityTypeRegistrarImpl {

    private final String namespace;

    public NeoForgeBalmEntityTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
        this.namespace = namespace;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void registerDefaultAttributes(Holder<EntityType<T>> entityType, Supplier<AttributeSupplier.Builder> attributes) {
        final var registrations = getActiveRegistrations();
        registrations.attributeSuppliers.put((Holder<@NotNull EntityType<? extends @NotNull LivingEntity>>) (Holder<?>) entityType, () -> attributes.get().build());
    }

    @Override
    protected <T extends Entity> void registerSpawnPlacement(Holder<EntityType<T>> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, Supplier<SpawnPlacements.SpawnPredicate<T>> attributesFunction) {
        final var registrations = getActiveRegistrations();
        registrations.spawnPlacements.add(new SpawnPlacementRegistration<>(entityType, spawnPlacementType, heightmapType, attributesFunction.get()));
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
    }

    public record SpawnPlacementRegistration<T extends Entity>(Holder<EntityType<T>> entityType, SpawnPlacementType spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<? extends Entity> predicate) {
    }

    public static class Registrations {
        public final Map<Holder<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier>> attributeSuppliers = new HashMap<>();
        public final List<SpawnPlacementRegistration<? extends Entity>> spawnPlacements = new ArrayList<>();

        @SubscribeEvent
        public void registerAttributes(EntityAttributeCreationEvent event) {
            for (final var entry : attributeSuppliers.entrySet()) {
                event.put(entry.getKey().value(), entry.getValue().get());
            }
        }

        @SubscribeEvent
        @SuppressWarnings("unchecked")
        public void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
            for (final var entry : spawnPlacements) {
                event.register((EntityType<Mob>) entry.entityType.value(), entry.spawnPlacementType, entry.heightmapType, (SpawnPlacements.SpawnPredicate<Mob>) entry.predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
            }
        }
    }
}
