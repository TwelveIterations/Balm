package net.blay09.mods.balm.forge.core.internal;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeferredRegisters {
    private static final Table<ResourceKey<?>, String, DeferredRegister<?>> deferredRegisters = Tables.synchronizedTable(HashBasedTable.create());
    private static final Table<ResourceKey<?>, String, Map<Identifier, Identifier>> aliases = Tables.synchronizedTable(HashBasedTable.create());

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

    public static void addAlias(String modId, ResourceKey<?> registryKey, Identifier oldId, Identifier newId) {
        synchronized (aliases) {
            var aliasMap = aliases.get(registryKey, modId);
            if (aliasMap == null) {
                aliasMap = new LinkedHashMap<>();
                aliases.put(registryKey, modId, aliasMap);
            }
            aliasMap.put(oldId, newId);
        }
    }

    public static Identifier getAliasTarget(String modId, ResourceKey<?> registryKey, Identifier oldId) {
        final var aliasMap = aliases.get(registryKey, modId);
        return aliasMap != null ? aliasMap.get(oldId) : null;
    }

    public static void register(String modId, BusGroup modEventBus) {
        synchronized (deferredRegisters) {
            for (DeferredRegister<?> deferredRegister : DeferredRegisters.getByModId(modId)) {
                deferredRegister.register(modEventBus);
            }
        }
    }
}
