package net.blay09.mods.balm.forge.core.internal;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegister;

public class ForgeRegistryAliasRemapper implements ModBusEventRegister {

    private final String modId;

    public ForgeRegistryAliasRemapper(String modId) {
        this.modId = modId;
    }

    @Override
    public void register(BusGroup busGroup) {
        MissingMappingsEvent.BUS.addListener(this::remapMissingMappings);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void remapMissingMappings(MissingMappingsEvent event) {
        remapMissingMappings((ResourceKey) event.getKey(), event);
    }

    private <T> void remapMissingMappings(ResourceKey<? extends Registry<T>> registryKey, MissingMappingsEvent event) {
        for (final var mapping : event.getAllMappings(registryKey)) {
            final var targetId = DeferredRegisters.getAliasTarget(modId, registryKey, mapping.getKey());
            if (targetId == null) {
                continue;
            }

            final var target = mapping.getRegistry().getValue(targetId);
            if (target != null) {
                mapping.remap(target);
            }
        }
    }
}
