package net.blay09.mods.balm.api.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public interface BalmSounds {
    DeferredObject<SoundEvent> register(ResourceLocation identifier);
}
