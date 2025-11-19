package net.blay09.mods.balm;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.platform.event.BidirectionalEventMapper;

import java.util.function.Consumer;

public class Balmstrap {
    /**
     * Register a callback to run when Balm is ready. This is for third party mods that do not use Balm but want to interact with it.
     * <p>
     * Mods building on Balm should use {@link Balm#initializeMod(String, BalmRuntimeLoadContext, java.util.function.Consumer)} instead.
     *
     * @param callback the callback to run when Balm is ready and its methods can be safely accessed.
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, java.util.function.Consumer)
     */
    public static void onRuntimeAvailable(Runnable callback) {
        // TODO In the future, we could avoid the potentially early class load here by having the runtime pull from this class instead.
        Balm.getRuntime().onRuntimeAvailable(callback);
    }

    public static <TEvent> BidirectionalEventMapper<Consumer<TEvent>> createBoundCustomEvent(Class<TEvent> eventClass) {
        return Balm.getRuntime().createBoundCustomEvent(eventClass);
    }
}
