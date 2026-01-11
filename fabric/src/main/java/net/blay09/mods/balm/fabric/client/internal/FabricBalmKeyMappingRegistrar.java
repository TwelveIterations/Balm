package net.blay09.mods.balm.fabric.client.internal;

import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class FabricBalmKeyMappingRegistrar implements BalmKeyMappingRegistrar {

    public static final FabricBalmKeyMappingRegistrar INSTANCE = new FabricBalmKeyMappingRegistrar();

    @Override
    public KeyMapping register(KeyMapping keyMapping) {
        return KeyMappingHelper.registerKeyBinding(keyMapping);
    }
}
