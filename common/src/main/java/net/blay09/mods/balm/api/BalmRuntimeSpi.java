package net.blay09.mods.balm.api;

import java.util.ServiceLoader;

public class BalmRuntimeSpi {
    public static BalmRuntime create() {
        var loader = ServiceLoader.load(BalmRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return factory.create();
    }
}
