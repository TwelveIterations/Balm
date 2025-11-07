package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.event.internal.AsymmetricalEventMapperImpl;
import net.minecraftforge.eventbus.api.bus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ForgeCustomEventMapper<T> extends AsymmetricalEventMapperImpl<Consumer<T>, Consumer<T>> {

    private final EventBus<@NotNull ForgifiedEvent<T>> bus;

    @SuppressWarnings("unchecked")
    public ForgeCustomEventMapper(EventBus<?> bus) {
        this.bus = (EventBus<@NotNull ForgifiedEvent<T>>) bus;
    }

    public EventBus<@NotNull ForgifiedEvent<T>> toEventBus() {
        return bus;
    }

}
