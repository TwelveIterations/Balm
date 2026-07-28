package net.blay09.mods.balm.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.Collection;

public class DeferredRegisters {
    private static final Table<ResourceKey<?>, String, DeferredRegister<?>> deferredRegisters = Tables.synchronizedTable(HashBasedTable.create());
    private static final Table<ResourceKey<?>, ResourceLocation, ResourceLocation> aliases = Tables.synchronizedTable(HashBasedTable.create());

    public static <T> DeferredRegister<T> get(IForgeRegistry<T> registry, String modId) {
        return get(registry.getRegistryKey(), modId);
    }

    @SuppressWarnings("unchecked")
    public static <T> DeferredRegister<T> get(ResourceKey<? extends Registry<T>> registry, String modId) {
        DeferredRegister<?> register = deferredRegisters.get(registry, modId);
        if (register == null) {
            register = DeferredRegister.create(registry, modId);
            deferredRegisters.put(registry, modId, register);
        }

        return (DeferredRegister<T>) register;
    }

    public static Collection<DeferredRegister<?>> getByModId(String modId) {
        return deferredRegisters.column(modId).values();
    }

    public static void addAlias(ResourceKey<?> registryKey, ResourceLocation oldId, ResourceLocation newId) {
        synchronized (aliases) {
            aliases.put(registryKey, oldId, newId);
        }
    }

    public static void register(String modId, IEventBus modEventBus) {
        synchronized (deferredRegisters) {
            for (DeferredRegister<?> deferredRegister : DeferredRegisters.getByModId(modId)) {
                deferredRegister.register(modEventBus);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void remapMissingMappings(MissingMappingsEvent event) {
        final var registryKey = (ResourceKey<? extends Registry<Object>>) event.getKey();
        synchronized (aliases) {
            for (final var alias : aliases.row(registryKey).entrySet()) {
                final var oldId = alias.getKey();
                final var targetId = alias.getValue();
                for (final var mapping : event.getMappings(registryKey, oldId.getNamespace())) {
                    if (mapping.getKey().equals(oldId)) {
                        final var target = mapping.getRegistry().getValue(targetId);
                        if (target != null) {
                            mapping.remap(target);
                        }
                    }
                }
            }
        }
    }

    public static void registerAliasRemapper() {
        MinecraftForge.EVENT_BUS.addListener(DeferredRegisters::remapMissingMappings);
    }
}
