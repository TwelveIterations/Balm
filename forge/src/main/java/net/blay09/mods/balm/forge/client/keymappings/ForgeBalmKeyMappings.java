package net.blay09.mods.balm.forge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public record ForgeBalmKeyMappings(NamespaceResolver namespaceResolver) implements BalmKeyMappings {

    @Override
    public KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, String category) {
        KeyMapping keyMapping = new KeyMapping(id.getPath(), type, keyCode, category);
        getRegistrations(id.getNamespace()).keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public BalmKeyMappings scoped(String modId) {
        return new ForgeBalmKeyMappings(new StaticNamespaceResolver(modId));
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final List<KeyMapping> keyMappings = new ArrayList<>();

        @SubscribeEvent
        public void registerKeyMappings(RegisterKeyMappingsEvent event) {
            keyMappings.forEach(event::register);
        }
    }

}
