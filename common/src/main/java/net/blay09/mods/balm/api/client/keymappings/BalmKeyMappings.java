package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar} instead.
 */
@Deprecated
public interface BalmKeyMappings {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar} instead.
     */
    @Deprecated
    default KeyMapping registerKeyMapping(ResourceLocation id, int keyCode, KeyMapping.Category category) {
        return registerKeyMapping(id, InputConstants.Type.KEYSYM, keyCode, category);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar} instead.
     */
    @Deprecated
    KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, KeyMapping.Category category);

    default BalmKeyMappings scoped(String modId) {
        return this;
    }
}
