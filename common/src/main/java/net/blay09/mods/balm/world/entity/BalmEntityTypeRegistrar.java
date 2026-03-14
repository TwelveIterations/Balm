package net.blay09.mods.balm.world.entity;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface BalmEntityTypeRegistrar {
    void addAlias(Identifier oldId, Identifier newId);

    void addAlias(String oldName, String newName);

    <T extends Entity> BalmEntityTypeRegistration<T> register(String name, Supplier<EntityType.Builder<T>> builder);
}
