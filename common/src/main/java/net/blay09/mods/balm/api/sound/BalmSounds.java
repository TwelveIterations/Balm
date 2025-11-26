package net.blay09.mods.balm.api.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registrar(ResourceKey)} with {@link net.minecraft.core.registries.Registries#SOUND_EVENT} instead.
 */
@Deprecated
public interface BalmSounds {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registrar(ResourceKey)} with {@link net.minecraft.core.registries.Registries#SOUND_EVENT} instead.
     */
    @Deprecated
    DeferredObject<SoundEvent> register(ResourceLocation identifier);
}
