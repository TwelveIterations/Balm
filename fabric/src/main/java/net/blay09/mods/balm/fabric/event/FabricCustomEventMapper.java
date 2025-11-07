package net.blay09.mods.balm.fabric.event;

import net.blay09.mods.balm.event.internal.AsymmetricalEventMapperImpl;
import net.fabricmc.fabric.api.event.Event;


import java.util.function.Consumer;

public class FabricCustomEventMapper<T> extends AsymmetricalEventMapperImpl<Consumer<T>, Consumer<T>> {
    private final Event<Consumer<T>> event;

    @SuppressWarnings("unchecked")
    public FabricCustomEventMapper(Event<?> event) {
        this.event = (Event<Consumer<T>>) event;
    }

    public Event<Consumer<T>> toEvent() {
        return event;
    }
}
