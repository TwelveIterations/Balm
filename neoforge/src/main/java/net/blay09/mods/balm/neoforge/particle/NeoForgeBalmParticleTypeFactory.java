package net.blay09.mods.balm.neoforge.particle;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.particle.internal.AbstractBalmParticleTypeFactoryImpl;
import net.minecraft.core.particles.SimpleParticleType;

public class NeoForgeBalmParticleTypeFactory extends AbstractBalmParticleTypeFactoryImpl {

    public NeoForgeBalmParticleTypeFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public SimpleParticleType createSimple(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }
}
