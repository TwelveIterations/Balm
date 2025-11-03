package net.blay09.mods.balm.neoforge.entity;

import net.blay09.mods.balm.api.entity.internal.AbstractBalmEntityTypeFactoryImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class NeoForgeBalmEntityTypeFactory extends AbstractBalmEntityTypeFactoryImpl {

    private final String namespace;

    public NeoForgeBalmEntityTypeFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
        this.namespace = namespace;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void registerDefaultAttributes(Holder<EntityType<T>> entityType, Function<AttributeSupplier.Builder, AttributeSupplier.Builder> attributes) {
        final var registrations = getActiveRegistrations();
        registrations.attributeSuppliers.put((Holder<EntityType<? extends LivingEntity>>) (Holder<?>) entityType, attributes.apply(AttributeSupplier.builder()).build());
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
    }

    public static class Registrations {
        public final Map<Holder<EntityType<? extends LivingEntity>>, AttributeSupplier> attributeSuppliers = new HashMap<>();

        @SubscribeEvent
        public void registerAttributes(EntityAttributeCreationEvent event) {
            for (final var entry : attributeSuppliers.entrySet()) {
                event.put(entry.getKey().value(), entry.getValue());
            }
        }
    }
}
