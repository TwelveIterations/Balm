package net.blay09.mods.balm.platform.config.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenProvider;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public final class BalmConfigScreenProviders {

    private static final Map<String, BalmConfigScreenProvider> providers = new ConcurrentHashMap<>();
    private static final Map<String, BalmConfigScreenFactory> modOverrides = new ConcurrentHashMap<>();

    private BalmConfigScreenProviders() {
    }

    public static void register(String providerId, BalmConfigScreenProvider provider) {
        providers.put(providerId, provider);
    }

    public static void registerModOverride(String modId, BalmConfigScreenFactory factory) {
        modOverrides.put(modId, factory);
    }

    public static boolean hasModOverride(String modId) {
        return modOverrides.containsKey(modId);
    }

    @Nullable
    public static BalmConfigScreenFactory getFactory(String modId, String providerId) {
        final var provider = providers.get(providerId);
        return provider != null ? provider.factory(modId) : null;
    }

    @Nullable
    public static BalmConfigScreenFactory getFactory(String modId, List<String> providerIds) {
        final var modFactory = modOverrides.get(modId);
        if (modFactory != null) {
            return modFactory;
        }

        for (final var providerId : providerIds) {
            final var factory = getFactory(modId, providerId);
            if (factory != null) {
                return factory;
            }
        }

        return null;
    }

    @Nullable
    public static BalmConfigScreenFactory getFactory(String modId) {
        return getFactory(modId, Balm.config().getPreferredConfigScreenProviders(modId));
    }

    public static Collection<String> getProviderIds() {
        return providers.keySet();
    }

    public static Set<Map.Entry<String, BalmConfigScreenFactory>> getModOverrides() {
        return modOverrides.entrySet();
    }

    public static Set<String> getConfigurableModIds() {
        final var result = new TreeSet<String>();
        Balm.config().getSchemas().forEach(schema -> result.add(schema.identifier().getNamespace()));
        result.addAll(modOverrides.keySet());
        result.removeIf(modId -> getFactory(modId) == null);
        return result;
    }
}
