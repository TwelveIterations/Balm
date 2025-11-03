package net.blay09.mods.balm.api.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface BalmEntityTypeFactory {
    <T extends Entity> BalmEntityTypeRegistration<T> register(String name, Supplier<EntityType.Builder<T>> builder);
}
