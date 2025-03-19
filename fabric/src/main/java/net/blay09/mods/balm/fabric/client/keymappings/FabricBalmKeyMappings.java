package net.blay09.mods.balm.fabric.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FabricBalmKeyMappings implements BalmKeyMappings {
    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }
}
