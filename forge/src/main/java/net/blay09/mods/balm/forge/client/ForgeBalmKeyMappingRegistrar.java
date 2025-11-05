package net.blay09.mods.balm.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class ForgeBalmKeyMappingRegistrar implements BalmKeyMappingRegistrar {

    private final RegisterKeyMappingsEvent event;

    public ForgeBalmKeyMappingRegistrar(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public KeyMapping register(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        KeyMapping keyMapping = new KeyMapping(name, type, keyCode, category);
        event.register(keyMapping);
        return keyMapping;
    }

}
