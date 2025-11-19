package net.blay09.mods.balm.platform.event;

import net.blay09.mods.balm.Balmstrap;
import net.blay09.mods.balm.platform.event.internal.EventMapperImpl;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Provides a uni-directional mapping from common code to a loader specific event.
 * <p>
 * Register event handlers using {@link #register(Object)}.
 *
 * @param <TCallback> The signature of the listener to be registered.
 */
public interface EventMapper<TCallback> {

    static <TCallback> EventMapper<TCallback> createUnbound(String name) {
        return new EventMapperImpl<>(name);
    }

    static <TEvent> BidirectionalEventMapper<Consumer<TEvent>> createBound(Class<TEvent> eventClass) {
        return Balmstrap.createBoundCustomEvent(eventClass);
    }

    /**
     * Registers the listener on the default priority phase.
     *
     * @param listener the listener to register to the event.
     */
    void register(TCallback listener);

    /**
     * Registers the listener on the given priority phase.
     *
     * @param phase    the priority phase to register the listener under.
     * @param listener the listener to register to the event.
     * @see EventPhases
     */
    void register(Identifier phase, TCallback listener);

    /**
     * For internal use by Balm or custom event mappers. Binds a registrar and invoker callback to this event.
     *
     * @param registrar The consumer that takes the priority phase and listener and registers it to the mod-loader specific bus.
     */
    @ApiStatus.Internal
    void configureMapping(BiConsumer<Identifier, TCallback> registrar);

    /**
     * For internal use by Balm. Returns the name of this event mapper.
     *
     * @return the name of this event mapper.
     */
    String name();
}
