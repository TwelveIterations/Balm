package net.blay09.mods.balm.event;

import net.blay09.mods.balm.event.internal.AsymmetricalEventMapperImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Provides a bidirectional mapping from common code to a loader specific event.
 * <p>
 * Register event handlers using {@link #register(Object)} or fire events into the mod loader bus by calling {@link #invoker()}.
 *
 * @param <TCallback> The signature of the listener to be registered.
 * @param <TInvoker>  Usually the same as the listener, but some events may need additional context to universally fire into mod loaders.
 */
public interface AsymmetricalEventMapper<TCallback, TInvoker> {

    static <TCallback, TInvoker> AsymmetricalEventMapper<TCallback, TInvoker> createUnbound() {
        return new AsymmetricalEventMapperImpl<>();
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
    void register(ResourceLocation phase, TCallback listener);

    /**
     * Provides a callable invoker that will fire this event into its backing event bus.
     *
     * @return an invoker that can be called to fire this event.
     */
    TInvoker invoker();

    /**
     * For internal use by Balm or custom event mappers. Binds a registrar and invoker callback to this event.
     *
     * @param registrar The consumer that takes the priority phase and listener and registers it to the mod-loader specific bus.
     * @param invoker   The invoker that takes the incoming parameters and fires them into the mod-loader specific bus.
     */
    @ApiStatus.Internal
    void setup(BiConsumer<ResourceLocation, TCallback> registrar, Supplier<TInvoker> invoker);
}
