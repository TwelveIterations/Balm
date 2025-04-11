package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

/**
 * Barebones implementation of Key Mappings.
 * For features like conflict contexts and key modifiers, use {@link net.blay09.mods.kuma.api.Kuma} instead.
 */
public interface BalmKeyMappings {
    default KeyMapping registerKeyMapping(ResourceLocation id, int keyCode, String category) {
        return registerKeyMapping(id, InputConstants.Type.KEYSYM, keyCode, category);
    }

    KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, String category);

    BalmKeyMappings scoped(String modId);
}
