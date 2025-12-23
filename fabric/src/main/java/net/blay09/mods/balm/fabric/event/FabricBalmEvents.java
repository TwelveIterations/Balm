package net.blay09.mods.balm.fabric.event;


import com.google.common.collect.*;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FabricBalmEvents implements BalmEvents {

    private final Map<Class<?>, Runnable> eventInitializers = new ConcurrentHashMap<>();
    private final Map<Class<?>, Consumer<?>> eventDispatchers = new ConcurrentHashMap<>();
    private final Multimap<Class<?>, Consumer<?>> eventHandlers = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
    private final Table<TickType<?>, TickPhase, Consumer<?>> tickEventInitializers = Tables.synchronizedTable(HashBasedTable.create());

    public void registerEvent(Class<?> eventClass, Runnable initializer) {
        registerEvent(eventClass, initializer, null);
    }

    public void registerEvent(Class<?> eventClass, Runnable initializer, @Nullable Consumer<?> dispatcher) {
        eventInitializers.put(eventClass, initializer);
        if (dispatcher != null) {
            eventDispatchers.put(eventClass, dispatcher);
        }
    }

    public <T> void fireEventHandlers(T event) {
        eventHandlers.get(event.getClass()).forEach(handler -> fireEventHandler(handler, event));
    }

    @SuppressWarnings("unchecked")
    private <T> void fireEventHandler(Consumer<T> handler, Object event) {
        handler.accept((T) event);
    }

    @Override
    public <T> void onEvent(Class<T> eventClass, Consumer<T> handler, EventPriority priority) {
        Runnable initializer = eventInitializers.remove(eventClass);
        if (initializer != null) {
            initializer.run();
        }

        eventHandlers.put(eventClass, handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void fireEvent(T event) {
        Consumer<T> handler = (Consumer<T>) eventDispatchers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        } else {
            fireEventHandlers(event);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void onTickEvent(TickType<T> type, TickPhase phase, T handler) {
        Consumer<T> initializer = (Consumer<T>) tickEventInitializers.get(type, phase);
        initializer.accept(handler);
    }

    public <T> void registerTickEvent(TickType<?> type, TickPhase phase, Consumer<T> initializer) {
        tickEventInitializers.put(type, phase, initializer);
    }

}
