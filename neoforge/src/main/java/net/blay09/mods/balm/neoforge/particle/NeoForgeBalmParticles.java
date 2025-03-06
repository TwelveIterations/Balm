package net.blay09.mods.balm.neoforge.particle;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class NeoForgeBalmParticles implements BalmParticles {
    @Override
    public <T extends ParticleOptions> DeferredObject<ParticleType<T>> registerParticle(Function<ResourceLocation, ParticleType<T>> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.PARTICLE_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> supplier.apply(identifier));
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    @Override
    public SimpleParticleType createSimple(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }
}
