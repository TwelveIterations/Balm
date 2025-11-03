package net.blay09.mods.balm.api.entity;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public interface BalmEntityTypeRegistration<T extends Entity> extends BalmHolderRegistration<EntityType<T>> {
    BalmEntityTypeRegistration<T> withDefaultAttributes(Supplier<AttributeSupplier.Builder> attributes);
}
