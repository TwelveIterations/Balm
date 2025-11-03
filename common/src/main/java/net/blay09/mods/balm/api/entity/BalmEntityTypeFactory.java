package net.blay09.mods.balm.api.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface BalmEntityTypeFactory {
    <T extends Entity> BalmEntityTypeRegistration<T> register(String name, EntityType.Builder<T> builder);
}
