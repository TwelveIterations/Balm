package net.blay09.mods.balm.neoforge.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public record NeoForgeBalmEntities(NamespaceResolver namespaceResolver) implements BalmEntities {

    @Override
    public <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder) {
        final var register = DeferredRegisters.get(Registries.ENTITY_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> typeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, identifier)));
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    @Override
    public <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        final var register = DeferredRegisters.get(Registries.ENTITY_TYPE, identifier.getNamespace());
        final var registrations = getActiveRegistrations();
        final var registryObject = register.register(identifier.getPath(), () -> {
            EntityType<T> entityType = typeBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, identifier));
            registrations.attributeSuppliers.put(entityType, attributeBuilder.get().build());
            return entityType;
        });
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final Map<EntityType<? extends LivingEntity>, AttributeSupplier> attributeSuppliers = new HashMap<>();

        @SubscribeEvent
        public void registerAttributes(EntityAttributeCreationEvent event) {
            for (final var entry : attributeSuppliers.entrySet()) {
                event.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
