package net.blay09.mods.balm.forge.client.internal;

import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

public class ForgeBalmKeyMappingRegistrar implements BalmKeyMappingRegistrar {

    private final RegisterKeyMappingsEvent event;

    public ForgeBalmKeyMappingRegistrar(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public KeyMapping register(KeyMapping keyMapping) {
        event.register(keyMapping);
        return keyMapping;
    }

}
