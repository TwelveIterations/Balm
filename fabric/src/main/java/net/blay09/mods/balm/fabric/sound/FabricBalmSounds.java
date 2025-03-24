package net.blay09.mods.balm.fabric.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class FabricBalmSounds implements BalmSounds {
    @Override
    public DeferredObject<SoundEvent> register(ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(identifier);
            return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, soundEvent);
        }).resolveImmediately();
    }
}
