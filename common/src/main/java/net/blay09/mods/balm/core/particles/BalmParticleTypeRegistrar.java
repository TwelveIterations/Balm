package net.blay09.mods.balm.core.particles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface BalmParticleTypeRegistrar {
    <TOptions extends ParticleOptions, TType extends ParticleType<TOptions>> BalmParticleTypeRegistration<TType> register(String name, Function<ResourceLocation, TType> constructor);
    
    BalmParticleTypeRegistration<SimpleParticleType> register(String name, boolean overrideLimiter);

    SimpleParticleType createSimple(boolean overrideLimiter);
}
