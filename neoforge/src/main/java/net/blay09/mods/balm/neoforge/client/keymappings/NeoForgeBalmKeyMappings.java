package net.blay09.mods.balm.neoforge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public record NeoForgeBalmKeyMappings(NamespaceResolver namespaceResolver) implements BalmKeyMappings {

    @Override
    public KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        KeyMapping keyMapping = new KeyMapping(id.getPath(), type, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public BalmKeyMappings scoped(String modId) {
        return new NeoForgeBalmKeyMappings(new StaticNamespaceResolver(modId));
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
