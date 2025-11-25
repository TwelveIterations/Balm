package net.blay09.mods.balm.fabric.world.entity.internal;

import net.blay09.mods.balm.world.entity.internal.AbstractBalmEntityTypeRegistrarImpl;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmEntityTypeRegistrar extends AbstractBalmEntityTypeRegistrarImpl {
    public FabricBalmEntityTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Entity> void registerDefaultAttributes(Holder<EntityType<T>> entityTypeHolder, Supplier<AttributeSupplier.Builder> attributes) {
        final var entityType = entityTypeHolder.value();
        FabricDefaultAttributeRegistry.register((EntityType<? extends @NotNull LivingEntity>) entityType, attributes.get());
    }
}
