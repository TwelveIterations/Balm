package net.blay09.mods.balm.api.event;

import java.util.function.Consumer;

@Deprecated
public interface BalmEvents {
    @Deprecated
    default <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        onEvent(eventClass, handler, EventPriority.Normal);
    }

    @Deprecated
    <T> void onEvent(Class<T> eventClass, Consumer<T> handler, EventPriority priority);

    @Deprecated
    <T> void fireEvent(T event);

    /**
     * @deprecated Use the new event mappers instead
     *
     * @see net.blay09.mods.balm.event.callback.ServerTickCallback
     * @see net.blay09.mods.balm.event.callback.ServerLevelTickCallback
     * @see net.blay09.mods.balm.event.callback.ServerPlayerTickCallback
     * @see net.blay09.mods.balm.event.callback.ServerEntityTickCallback
     * @see net.blay09.mods.balm.client.event.callback.ClientTickCallback
     * @see net.blay09.mods.balm.client.event.callback.ClientLevelTickCallback
     * @see net.blay09.mods.balm.client.event.callback.ClientPlayerTickCallback
     * @see net.blay09.mods.balm.client.event.callback.ClientEntityTickCallback
     */
    @Deprecated
    <T> void onTickEvent(TickType<T> type, TickPhase phase, T handler);
}
