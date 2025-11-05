package net.blay09.mods.balm.neoforge.client.particle;

import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.function.Function;

public class NeoForgeBalmParticleProviderRegistrar implements BalmParticleProviderRegistrar {
    private final RegisterParticleProvidersEvent event;

    public NeoForgeBalmParticleProviderRegistrar(RegisterParticleProvidersEvent event) {
        this.event = event;
    }

    @Override
    public <T extends ParticleOptions> void register(Holder<ParticleType<T>> particleType, Function<SpriteSet, ParticleProvider<T>> factory) {
        event.registerSpriteSet(particleType.value(), factory::apply);
    }

    @Override
    public <T extends ParticleOptions> void register(Holder<ParticleType<T>> particleType, ParticleProvider<T> provider) {
        event.registerSpriteSet(particleType.value(), spriteSet -> provider);
    }
}
