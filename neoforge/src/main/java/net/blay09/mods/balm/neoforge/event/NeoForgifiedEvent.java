package net.blay09.mods.balm.neoforge.event;

import net.neoforged.bus.api.Event;

public class NeoForgifiedEvent<T> extends Event {
    private final T data;

    public NeoForgifiedEvent(T data) {
        this.data = data;
    }

    public T data() {
        return data;
    }
}
