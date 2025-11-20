package net.blay09.mods.balm.fabric.client.internal.renderer.entity;

import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class FabricBalmEntityRendererRegistrar implements BalmEntityRendererRegistrar {

    public static final BalmEntityRendererRegistrar INSTANCE = new FabricBalmEntityRendererRegistrar();

    @Override
    public <T extends Entity> void register(Holder<? extends EntityType<? extends T>> entityTypeHolder, EntityRendererProvider<? super T> provider) {
        EntityRenderers.register(entityTypeHolder.value(), provider);
    }

    @Override
    public <T extends Entity> void register(String name, Supplier<? extends EntityType<? extends T>> entityTypeSupplier, EntityRendererProvider<? super T> provider) {
        EntityRenderers.register(entityTypeSupplier.get(), provider);
    }
}
