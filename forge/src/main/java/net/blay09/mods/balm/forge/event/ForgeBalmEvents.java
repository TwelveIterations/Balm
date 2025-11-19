package net.blay09.mods.balm.forge.event;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.minecraftforge.eventbus.api.listener.Priority;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Deprecated
public class ForgeBalmEvents implements BalmEvents {

    private final Table<Class<?>, EventPriority, Consumer<EventPriority>> eventInitializers = HashBasedTable.create();
    private final Map<Class<?>, Consumer<?>> eventDispatchers = new HashMap<>();
    private final Table<Class<?>, EventPriority, List<Consumer<?>>> eventHandlers = HashBasedTable.create();
    private final Table<TickType<?>, TickPhase, Consumer<?>> tickEventInitializers = HashBasedTable.create();

    public static byte toForge(EventPriority priority) {
        return switch (priority) {
            case Lowest -> Priority.LOWEST;
            case Low -> Priority.LOW;
            case Normal -> Priority.NORMAL;
            case High -> Priority.HIGH;
            case Highest -> Priority.HIGHEST;
        };
    }

    public void registerEvent(Class<?> eventClass, Consumer<EventPriority> initializer) {
        registerEvent(eventClass, initializer, null);
    }

    public void registerEvent(Class<?> eventClass, Consumer<EventPriority> initializer, @Nullable Consumer<?> dispatcher) {
        for (EventPriority priority : EventPriority.values()) {
            eventInitializers.put(eventClass, priority, initializer);
        }

        if (dispatcher != null) {
            eventDispatchers.put(eventClass, dispatcher);
        }
    }

    public <T> void fireEventHandlers(EventPriority priority, T event) {
        List<Consumer<?>> handlers = eventHandlers.get(event.getClass(), priority);
        if (handlers != null) {
            handlers.forEach(handler -> fireEventHandler(handler, event));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void fireEventHandler(Consumer<T> handler, Object event) {
        handler.accept((T) event);
    }

    @Override
    public <T> void onEvent(Class<T> eventClass, Consumer<T> handler, EventPriority priority) {
        Consumer<EventPriority> initializer = eventInitializers.remove(eventClass, priority);
        if (initializer != null) {
            initializer.accept(priority);
        }

        List<Consumer<?>> consumers = eventHandlers.get(eventClass, priority);
        if (consumers == null) {
            consumers = new ArrayList<>();
            eventHandlers.put(eventClass, priority, consumers);
        }
        consumers.add(handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void fireEvent(T event) {
        Consumer<T> handler = (Consumer<T>) eventDispatchers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        } else {
            for (EventPriority priority : EventPriority.values) {
                fireEventHandlers(priority, event);
            }
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
