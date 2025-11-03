package net.blay09.mods.balm.particle.internal;

import net.blay09.mods.balm.api.particle.BalmParticleTypeFactory;
import net.blay09.mods.balm.api.particle.BalmParticleTypeRegistration;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class AbstractBalmParticleTypeFactoryImpl implements BalmParticleTypeFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    public AbstractBalmParticleTypeFactoryImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <T extends ParticleOptions> BalmParticleTypeRegistration<T> register(String name, Function<ResourceLocation, ParticleType<T>> constructor) {
        final var id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var key = ResourceKey.create(Registries.PARTICLE_TYPE, id);
        final var holder = registrar.register(key, constructor::apply);
        return new BalmParticleTypeRegistrationImpl<>(holder);
    }

    private static class BalmParticleTypeRegistrationImpl<T extends ParticleOptions> implements BalmParticleTypeRegistration<T> {
        private final Holder<ParticleType<T>> holder;

        @SuppressWarnings("unchecked")
        private BalmParticleTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<ParticleType<T>>) holder;
        }

        @Override
        public Holder<ParticleType<T>> asHolder() {
            return holder;
        }
    }
}
