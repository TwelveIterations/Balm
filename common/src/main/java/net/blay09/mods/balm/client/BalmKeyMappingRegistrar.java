package net.blay09.mods.balm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface BalmKeyMappingRegistrar {

    default KeyMapping register(String name, int keyCode, String category) {
        return register(name, InputConstants.Type.KEYSYM, keyCode, category);
    }

    default KeyMapping register(String name, InputConstants.Type type, int keyCode, String category) {
        return register(new KeyMapping(name, type, keyCode, category));
    }

    KeyMapping register(KeyMapping keyMapping);
}
