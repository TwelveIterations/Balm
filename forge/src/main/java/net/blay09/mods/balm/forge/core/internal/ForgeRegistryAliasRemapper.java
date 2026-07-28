package net.blay09.mods.balm.forge.core.internal;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegister;

public class ForgeRegistryAliasRemapper implements ModBusEventRegister {
    private final Table<ResourceKey<?>, Identifier, Identifier> aliases = Tables.synchronizedTable(HashBasedTable.create());

    public void addAlias(ResourceKey<?> registryKey, Identifier oldId, Identifier newId) {
        synchronized (aliases) {
            aliases.put(registryKey, oldId, newId);
        }
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
            final var targetId = aliases.get(registryKey, mapping.getKey());
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
