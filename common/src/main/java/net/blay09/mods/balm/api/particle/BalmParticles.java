package net.blay09.mods.balm.api.particle;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#particleTypes(Consumer)} instead.
 */
@Deprecated
public interface BalmParticles {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#particleTypes(Consumer)} instead.
     */
    @Deprecated
    <T extends ParticleOptions> DeferredObject<ParticleType<T>> registerParticle(Function<ResourceLocation, ParticleType<T>> supplier, ResourceLocation identifier);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#particleTypes(Consumer)} instead.
     */
    @Deprecated
    SimpleParticleType createSimple(boolean overrideLimiter);
}
