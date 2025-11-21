package net.blay09.mods.balm.core.particles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public interface BalmParticleTypeRegistrar {
    <T extends ParticleOptions> BalmParticleTypeRegistration<T> register(String name, Function<Identifier, ParticleType<T>> constructor);
    
    BalmParticleTypeRegistration<SimpleParticleType> register(String name, boolean overrideLimiter);

    SimpleParticleType createSimple(boolean overrideLimiter);
}
