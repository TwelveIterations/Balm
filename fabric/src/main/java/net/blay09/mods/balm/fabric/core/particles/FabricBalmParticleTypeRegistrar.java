package net.blay09.mods.balm.fabric.core.particles;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.particles.internal.AbstractBalmParticleTypeRegistrarImpl;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class FabricBalmParticleTypeRegistrar extends AbstractBalmParticleTypeRegistrarImpl {

    public FabricBalmParticleTypeRegistrar(BalmRegistrar registrar, String namespace) {
        super(registrar, namespace);
    }

    @Override
    public SimpleParticleType createSimple(boolean overrideLimiter) {
        return FabricParticleTypes.simple(overrideLimiter);
    }

}
