package net.blay09.mods.balm.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public interface BalmParticleProviderRegistrar {

    <T extends ParticleOptions> void register(Holder<? extends ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory);

    <T extends ParticleOptions> void register(Holder<? extends ParticleType<T>> particleType, ParticleProvider<T> provider);
}
