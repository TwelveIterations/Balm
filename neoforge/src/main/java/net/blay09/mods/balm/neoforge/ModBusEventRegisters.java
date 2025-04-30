package net.blay09.mods.balm.neoforge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import net.neoforged.bus.api.IEventBus;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModBusEventRegisters {

    private static final Map<String, IEventBus> modEventBuses = new ConcurrentHashMap<>();
    private static final Table<String, Class<?>, Object> registrations = Tables.synchronizedTable(HashBasedTable.create());

    @SuppressWarnings("unchecked")
    public static <T> T getRegistrations(String namespace, Class<T> clazz) {
        final var existing = registrations.get(namespace, clazz);
        if (existing != null) {
            return (T) existing;
        }
        try {
            T instance;
            try {
                instance = clazz.getConstructor(String.class).newInstance(namespace);
            } catch (NoSuchMethodException e) {
                instance = clazz.getConstructor().newInstance();
            }
            registrations.put(namespace, clazz, instance);
            final var modEventBus = modEventBuses.get(namespace);
            if (modEventBus != null) {
                modEventBus.register(instance);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static void register(String modId, IEventBus modEventBus) {
        modEventBuses.put(modId, modEventBus);
        synchronized (registrations) {
            for (final var registrations : getByModId(modId)) {
                modEventBus.register(registrations);
            }
        }
    }

    private static Collection<Object> getByModId(String modId) {
        return registrations.row(modId).values();
    }
}
