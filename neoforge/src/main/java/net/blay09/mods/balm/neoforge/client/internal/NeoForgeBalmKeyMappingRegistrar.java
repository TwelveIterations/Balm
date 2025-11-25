package net.blay09.mods.balm.neoforge.client.internal;

import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class NeoForgeBalmKeyMappingRegistrar implements BalmKeyMappingRegistrar {

    private final RegisterKeyMappingsEvent event;

    public NeoForgeBalmKeyMappingRegistrar(RegisterKeyMappingsEvent event) {
        this.event = event;
    }

    @Override
    public KeyMapping register(KeyMapping keyMapping) {
        event.register(keyMapping);
        return keyMapping;
    }

}
