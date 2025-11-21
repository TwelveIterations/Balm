package net.blay09.mods.balm.core.particles.internal;

import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistration;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public abstract class AbstractBalmParticleTypeRegistrarImpl implements BalmParticleTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public AbstractBalmParticleTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <TOptions extends ParticleOptions, TType extends ParticleType<TOptions>> BalmParticleTypeRegistration<TType> register(String name, Function<Identifier, TType> constructor) {
        final var id = Identifier.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.PARTICLE_TYPE, id);
        final var holder = registrar.register(key, constructor::apply);
        return new BalmParticleTypeRegistrationImpl<>(holder);
    }

    public BalmParticleTypeRegistration<SimpleParticleType> register(String name, boolean overrideLimiter) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.PARTICLE_TYPE, identifier);
        final var holder = registrar.register(key, (id) -> createSimple(overrideLimiter));
        return new BalmParticleTypeRegistrationImpl<>(holder);
    }

    private static class BalmParticleTypeRegistrationImpl<TOptions extends ParticleOptions, TType extends ParticleType<TOptions>> implements BalmParticleTypeRegistration<TType> {
        private final Holder<TType> holder;

        @SuppressWarnings("unchecked")
        private BalmParticleTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<TType>) holder;
        }

        @Override
        public Holder<TType> asHolder() {
            return holder;
        }
    }
}
