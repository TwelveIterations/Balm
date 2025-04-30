package net.blay09.mods.balm.fabric.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class FabricBalmKeyMappings implements BalmKeyMappings {
    @Override
    public KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(id.getPath(), type, keyCode, category));
    }

    @Override
    public BalmKeyMappings scoped(String modId) {
        return this;
    }
}
