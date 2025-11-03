package net.blay09.mods.balm.core.particles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface BalmParticleTypeRegistrar {
    <T extends ParticleOptions> BalmParticleTypeRegistration<T> register(String name, Function<ResourceLocation, ParticleType<T>> constructor);

    SimpleParticleType createSimple(boolean overrideLimiter);
}
