package net.blay09.mods.balm.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface BalmEntityRendererRegistrar {

    <T extends Entity> void register(Holder<? extends EntityType<? extends T>> entityTypeHolder, EntityRendererProvider<? super T> provider);

    <T extends Entity> void register(String name, Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<? super T> provider);
}
