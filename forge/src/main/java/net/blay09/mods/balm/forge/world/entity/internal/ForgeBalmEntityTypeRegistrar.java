package net.blay09.mods.balm.forge.world.entity.internal;

import net.blay09.mods.balm.world.entity.internal.AbstractBalmEntityTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegister;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegisters;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeBalmEntityTypeRegistrar extends AbstractBalmEntityTypeRegistrarImpl {

    private final String namespace;

    public ForgeBalmEntityTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
        this.namespace = namespace;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void registerDefaultAttributes(Holder<EntityType<T>> entityType, Supplier<AttributeSupplier.Builder> attributes) {
        final var registrations = getActiveRegistrations();
        registrations.attributeSuppliers.put((Holder<EntityType<? extends LivingEntity>>) (Holder<?>) entityType, () -> attributes.get().build());
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

    public static class Registrations implements ModBusEventRegister {
        public final Map<Holder<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier>> attributeSuppliers = new HashMap<>();
        public final List<SpawnPlacementRegistration<? extends Entity>> spawnPlacements = new ArrayList<>();

        private void registerAttributes(EntityAttributeCreationEvent event) {
            for (final var entry : attributeSuppliers.entrySet()) {
                event.put(entry.getKey().value(), entry.getValue().get());
            }
        }

        private void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
            for (final var entry : spawnPlacements) {
                event.register((EntityType<Mob>) entry.entityType.value(), entry.spawnPlacementType, entry.heightmapType, (SpawnPlacements.SpawnPredicate<Mob>) entry.predicate, SpawnPlacementRegisterEvent.Operation.REPLACE);
            }
        }

        @Override
        public void register(BusGroup busGroup) {
            EntityAttributeCreationEvent.BUS.addListener(this::registerAttributes);
            SpawnPlacementRegisterEvent.BUS.addListener(this::registerSpawnPlacements);
        }
    }
}
