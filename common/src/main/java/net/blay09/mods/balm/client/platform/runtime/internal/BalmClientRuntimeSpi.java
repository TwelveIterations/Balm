package net.blay09.mods.balm.client.platform.runtime.internal;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

public class BalmClientRuntimeSpi {
    @SuppressWarnings("unchecked")
    public static BalmClientRuntime<BalmRuntimeLoadContext> create() {
        var loader = ServiceLoader.load(BalmClientRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return (BalmClientRuntime<@NotNull BalmRuntimeLoadContext>) factory.create();
    }
}
