package net.blay09.mods.balm.event;

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
 */
public interface BidirectionalEventMapper<TCallback> extends EventMapper<TCallback> {

    /**
     * Provides a callable invoker that will fire this event into its backing event bus.
     *
     * @return an invoker that can be called to fire this event.
     */
    TCallback invoker();

    /**
     * For internal use by Balm or custom event mappers. Binds a registrar and invoker callback to this event.
     *
     * @param registrar The consumer that takes the priority phase and listener and registers it to the mod-loader specific bus.
     * @param invoker   The invoker that takes the incoming parameters and fires them into the mod-loader specific bus.
     */
    @ApiStatus.Internal
    void setup(BiConsumer<ResourceLocation, TCallback> registrar, Supplier<TCallback> invoker);

}
