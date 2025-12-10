package net.blay09.mods.balm.forge.world.entity;

import net.blay09.mods.balm.world.entity.internal.AbstractBalmEntityTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.forge.ModBusEventRegister;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
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
        registrations.attributeSuppliers.put((Holder<@NotNull EntityType<? extends @NotNull LivingEntity>>) (Holder<?>) entityType, () -> attributes.get().build());
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
    }

    public static class Registrations implements ModBusEventRegister {
        public final Map<Holder<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier>> attributeSuppliers = new HashMap<>();

        private void registerAttributes(EntityAttributeCreationEvent event) {
            for (final var entry : attributeSuppliers.entrySet()) {
                event.put(entry.getKey().value(), entry.getValue().get());
            }
        }

        @Override
        public void register(BusGroup busGroup) {
            EntityAttributeCreationEvent.BUS.addListener(this::registerAttributes);
        }
    }
}
