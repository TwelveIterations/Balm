package net.blay09.mods.balm.api.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#entityTypes(Consumer)} instead.
 */
@Deprecated
public interface BalmEntities {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#entityTypes(Consumer)} instead.
     */
    @Deprecated
    <T extends Entity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#entityTypes(Consumer)} instead.
     */
    @Deprecated
    <T extends LivingEntity> DeferredObject<EntityType<T>> registerEntity(ResourceLocation identifier, EntityType.Builder<T> typeBuilder, Supplier<AttributeSupplier.Builder> attributeBuilder);
}
