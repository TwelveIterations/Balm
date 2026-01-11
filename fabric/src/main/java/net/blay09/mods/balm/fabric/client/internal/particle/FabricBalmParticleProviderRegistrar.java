package net.blay09.mods.balm.fabric.client.internal.particle;

import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class FabricBalmParticleProviderRegistrar implements BalmParticleProviderRegistrar {
    public static final FabricBalmParticleProviderRegistrar INSTANCE = new FabricBalmParticleProviderRegistrar();

    @Override
    public <T extends ParticleOptions> void register(Holder<? extends ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        ParticleProviderRegistry.getInstance().register(particleType.value(), factory::apply);
    }

    @Override
    public <T extends ParticleOptions> void register(Holder<? extends ParticleType<T>> particleType, ParticleProvider<T> provider) {
        ParticleProviderRegistry.getInstance().register(particleType.value(), provider);
    }
}
