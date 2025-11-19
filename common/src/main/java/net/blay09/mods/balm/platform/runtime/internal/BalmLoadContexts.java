package net.blay09.mods.balm.platform.runtime.internal;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BalmLoadContexts {

    private static final Map<String, BalmRuntimeLoadContext> loadContexts = new ConcurrentHashMap<>();

    public static void register(String modId, BalmRuntimeLoadContext context) {
        loadContexts.put(modId, context);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BalmRuntimeLoadContext> Optional<T> get(String modId) {
        return Optional.ofNullable((T) loadContexts.get(modId));
    }
}
