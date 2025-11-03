package net.blay09.mods.balm.fabric.particle;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.particle.internal.AbstractBalmParticleTypeFactoryImpl;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class FabricBalmParticleTypeFactory extends AbstractBalmParticleTypeFactoryImpl {

    public FabricBalmParticleTypeFactory(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public SimpleParticleType createSimple(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

}
