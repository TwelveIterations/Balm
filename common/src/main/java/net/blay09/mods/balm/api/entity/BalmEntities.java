package net.blay09.mods.balm.api.entity;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

/**
 * @deprecated Use the scoped registrar via {@code Balm.entityTypes(namespace, initializer)} and {@link BalmEntityTypeRegistrar}
 */
@Deprecated
public interface BalmEntities {
    /**
     * @deprecated Use the scoped registrar via {@code Balm.entityTypes(namespace, initializer)} and {@link BalmEntityTypeRegistrar}
     */
    @Deprecated
    default <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder) {
        final var holder = Balm.getRuntime().entityTypes(identifier.getNamespace())
                .register(identifier.getPath(), () -> typeBuilder)
                .asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * @deprecated Use the scoped registrar via {@code Balm.entityTypes(namespace, initializer)} and {@link BalmEntityTypeRegistrar}
     */
    @Deprecated
    default <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        final var holder = Balm.getRuntime().entityTypes(identifier.getNamespace())
                .register(identifier.getPath(), () -> typeBuilder)
                .withDefaultAttributes((it) -> attributeBuilder.get())
                .asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmEntities LEGACY = new BalmEntities() { };
}
