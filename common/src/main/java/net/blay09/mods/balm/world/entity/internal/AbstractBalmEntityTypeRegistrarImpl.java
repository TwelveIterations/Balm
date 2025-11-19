package net.blay09.mods.balm.world.entity.internal;

import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistration;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBalmEntityTypeRegistrarImpl implements BalmEntityTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    protected AbstractBalmEntityTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    protected abstract <T extends Entity> void registerDefaultAttributes(Holder<EntityType<T>> entityType, Function<AttributeSupplier.Builder, AttributeSupplier.Builder> attributesFunction);

    @Override
    public <T extends Entity> BalmEntityTypeRegistration<T> register(String name, Supplier<EntityType.Builder<T>> builder) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, identifier);
        final var holder = registrar.register(resourceKey, id -> builder.get().build(resourceKey));
        return new BalmEntityTypeRegistrationImpl<>(holder);
    }

    private class BalmEntityTypeRegistrationImpl<T extends Entity> implements BalmEntityTypeRegistration<T> {
        private final Holder<EntityType<T>> holder;

        @SuppressWarnings("unchecked")
        private BalmEntityTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<@NotNull EntityType<T>>) holder;
        }

        @Override
        public Holder<EntityType<T>> asHolder() {
            return holder;
        }

        @Override
        public BalmEntityTypeRegistration<T> withDefaultAttributes(Function<AttributeSupplier.Builder, AttributeSupplier.Builder> attributesFunction) {
            registerDefaultAttributes(holder, attributesFunction);
            return this;
        }
    }
}
