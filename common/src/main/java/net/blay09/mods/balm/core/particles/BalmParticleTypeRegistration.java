package net.blay09.mods.balm.core.particles;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public interface BalmParticleTypeRegistration<T extends ParticleOptions> extends BalmHolderRegistration<ParticleType<T>> {
}
