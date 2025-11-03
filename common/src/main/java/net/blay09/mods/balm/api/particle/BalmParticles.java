package net.blay09.mods.balm.api.particle;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.particle.BalmParticleTypeFactory}
 *             via {@link net.blay09.mods.balm.api.Balm#particleTypes(String, java.util.function.Consumer)} instead.
 */
@Deprecated
public interface BalmParticles {
    default <T extends ParticleOptions> DeferredObject<ParticleType<T>> registerParticle(Function<ResourceLocation, ParticleType<T>> supplier, ResourceLocation identifier) {
        final var holder = Balm.getRuntime().particleTypes(identifier.getNamespace()).register(identifier.getPath(), supplier).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    default SimpleParticleType createSimple(boolean overrideLimiter) {
        return Balm.getRuntime().particleTypes("balm").createSimple(overrideLimiter);
    }

    BalmParticles LEGACY = new BalmParticles() {
    };
}
