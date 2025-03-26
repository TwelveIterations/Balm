package net.blay09.mods.balm.api.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public interface BalmEntities {
    <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder);

    <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder);

    @Deprecated
    default <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, AttributeSupplier.Builder attributeBuilder) {
        return registerEntity(identifier, typeBuilder, () -> attributeBuilder);
    }
}
