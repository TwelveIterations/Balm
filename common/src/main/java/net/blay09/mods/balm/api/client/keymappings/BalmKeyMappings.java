package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

/**
 * Barebones implementation of Key Mappings.
 * For features like conflict contexts and key modifiers, use {@link net.blay09.mods.kuma.api.Kuma} instead.
 */
public interface BalmKeyMappings {
    default KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return registerKeyMapping(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category);
}
