package net.blay09.mods.balm.api.particle;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface BalmParticles {
    <T extends ParticleOptions> DeferredObject<ParticleType<T>> registerParticle(Function<ResourceLocation, ParticleType<T>> supplier, ResourceLocation identifier);

    SimpleParticleType createSimple(boolean overrideLimiter);
}
