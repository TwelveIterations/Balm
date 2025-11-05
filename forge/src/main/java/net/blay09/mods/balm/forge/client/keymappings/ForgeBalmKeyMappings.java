package net.blay09.mods.balm.forge.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegister;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public record ForgeBalmKeyMappings(NamespaceResolver namespaceResolver) implements BalmKeyMappings {

    @Override
    public KeyMapping registerKeyMapping(ResourceLocation id, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        KeyMapping keyMapping = new KeyMapping(id.getPath(), type, keyCode, category);
        getActiveRegistrations().keyMappings.add(keyMapping);
        return keyMapping;
    }

    @Override
    public BalmKeyMappings scoped(String modId) {
        return new ForgeBalmKeyMappings(new StaticNamespaceResolver(modId));
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations implements ModBusEventRegister {
        public final List<KeyMapping> keyMappings = new ArrayList<>();

        private void registerKeyMappings(RegisterKeyMappingsEvent event) {
            keyMappings.forEach(event::register);
        }

        @Override
        public void register(BusGroup busGroup) {
            RegisterKeyMappingsEvent.BUS.addListener(this::registerKeyMappings);
        }
    }

}
