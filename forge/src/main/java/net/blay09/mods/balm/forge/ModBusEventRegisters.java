package net.blay09.mods.balm.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import net.minecraftforge.eventbus.api.bus.BusGroup;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModBusEventRegisters {

    private static final Map<String, BusGroup> modEventBuses = new ConcurrentHashMap<>();
    private static final Table<String, Class<?>, Object> registrations = Tables.synchronizedTable(HashBasedTable.create());
    private static final Set<Object> registeredRegistrations = Sets.newConcurrentHashSet();

    public static BusGroup getBusGroup(String namespace) {
        return modEventBuses.get(namespace);
    }

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
                registerToEventBus(instance, modEventBus);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(String modId, BusGroup modEventBus) {
        modEventBuses.put(modId, modEventBus);
        synchronized (registrations) {
            for (final var registrations : getByModId(modId)) {
                registerToEventBus(registrations, modEventBus);
            }
        }
    }

    private static <T> void registerToEventBus(T instance, BusGroup modEventBus) {
        if (!registeredRegistrations.add(instance)) {
            return;
        }

        if (instance instanceof ModBusEventRegister modBusEventRegister) {
            modBusEventRegister.register(modEventBus);
        } else {
            modEventBus.register(MethodHandles.lookup(), instance);
        }
    }

    private static Collection<Object> getByModId(String modId) {
        return registrations.row(modId).values();
    }
}
