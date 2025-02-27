package net.blay09.mods.balm.fabric.particle;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class FabricBalmParticles implements BalmParticles {

    @Override
    public <T extends ParticleOptions> DeferredObject<ParticleType<T>> registerParticle(Function<ResourceLocation, ParticleType<T>> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier,
                () -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier, supplier.apply(identifier))).resolveImmediately();
    }
}
