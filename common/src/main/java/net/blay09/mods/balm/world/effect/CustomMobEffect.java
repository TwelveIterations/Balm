package net.blay09.mods.balm.world.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * No different from a MobEffect, other than having public constructors for convenience.
 */
public class CustomMobEffect extends MobEffect {
    public CustomMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public CustomMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }
}
