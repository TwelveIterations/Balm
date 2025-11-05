package net.blay09.mods.balm.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public interface BalmKeyMappingRegistrar {

    default KeyMapping register(String name, int keyCode, KeyMapping.Category category) {
        return register(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    default KeyMapping register(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        return register(new KeyMapping(name, type, keyCode, category));
    }

    KeyMapping register(KeyMapping keyMapping);
}
