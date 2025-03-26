package net.blay09.mods.balm.api.client;

import java.util.ServiceLoader;

public class BalmClientRuntimeSpi {
    public static BalmClientRuntime create() {
        var loader = ServiceLoader.load(BalmClientRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return factory.create();
    }
}
