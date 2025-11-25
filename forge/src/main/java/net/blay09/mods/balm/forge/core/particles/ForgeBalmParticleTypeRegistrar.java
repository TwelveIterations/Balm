package net.blay09.mods.balm.forge.core.particles;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.particles.internal.AbstractBalmParticleTypeRegistrarImpl;
import net.minecraft.core.particles.SimpleParticleType;

public class ForgeBalmParticleTypeRegistrar extends AbstractBalmParticleTypeRegistrarImpl {

    public ForgeBalmParticleTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public SimpleParticleType createSimple(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }
}
