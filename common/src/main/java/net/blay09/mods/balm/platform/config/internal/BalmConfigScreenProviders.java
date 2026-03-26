package net.blay09.mods.balm.platform.config.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenProvider;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BalmConfigScreenProviders {

    private static final Map<String, BalmConfigScreenProvider> providers = new ConcurrentHashMap<>();

    private BalmConfigScreenProviders() {
    }

    public static void register(String providerId, BalmConfigScreenProvider provider) {
        providers.put(providerId, provider);
    }

    @Nullable
    public static BalmConfigScreenFactory getFactory(String modId, List<String> providerIds) {
        for (final var providerId : providerIds) {
            final var provider = providers.get(providerId);
            if (provider == null) {
                continue;
            }

            final var factory = provider.factory(modId);
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
}
