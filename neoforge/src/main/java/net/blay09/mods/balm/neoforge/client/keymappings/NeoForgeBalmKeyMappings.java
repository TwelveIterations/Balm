package net.blay09.mods.balm.neoforge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.minecraft.client.KeyMapping;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class NeoForgeBalmKeyMappings implements BalmKeyMappings {
    private static class Registrations {
        public final List<KeyMapping> keyMappings = new ArrayList<>();

        @SubscribeEvent
        public void registerKeyMappings(RegisterKeyMappingsEvent event) {
            keyMappings.forEach(event::register);
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(id.getPath(), type, keyCode, category);
        getRegistrations(id.getNamespace()).keyMappings.add(keyMapping);
        return keyMapping;
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

}
