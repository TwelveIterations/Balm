package net.blay09.mods.balm.world.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface BalmEntityTypeRegistrar {
    <T extends Entity> BalmEntityTypeRegistration<T> register(String name, Supplier<EntityType.Builder<T>> builder);

    void addAlias(ResourceLocation oldId, ResourceLocation newId);

    void addAlias(String oldName, String newName);
}
