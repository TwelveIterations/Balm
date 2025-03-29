package net.blay09.mods.balm.api;

import java.util.ServiceLoader;

public class BalmRuntimeSpi {
    @SuppressWarnings("unchecked")
    public static BalmRuntime<BalmRuntimeLoadContext> create() {
        var loader = ServiceLoader.load(BalmRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return (BalmRuntime<BalmRuntimeLoadContext>) factory.create();
    }
}
