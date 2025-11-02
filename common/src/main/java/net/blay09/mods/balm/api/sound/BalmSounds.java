package net.blay09.mods.balm.api.sound;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(net.minecraft.resources.ResourceKey, String)} instead.
 */
@Deprecated
public interface BalmSounds {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(net.minecraft.resources.ResourceKey, String)} instead.
     */
    @Deprecated
    default DeferredObject<SoundEvent> register(ResourceLocation identifier) {
        final var resourceKey = ResourceKey.create(Registries.SOUND_EVENT, identifier);
        final var holder = Balm.registrar().register(resourceKey, SoundEvent::createVariableRangeEvent);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmSounds LEGACY = new BalmSounds() {
    };
}
