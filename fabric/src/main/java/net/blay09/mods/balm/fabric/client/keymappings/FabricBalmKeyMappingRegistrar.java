package net.blay09.mods.balm.fabric.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FabricBalmKeyMappingRegistrar implements BalmKeyMappingRegistrar {

    public static final FabricBalmKeyMappingRegistrar INSTANCE = new FabricBalmKeyMappingRegistrar();

    @Override
    public KeyMapping register(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, type, keyCode, category));
    }
}
