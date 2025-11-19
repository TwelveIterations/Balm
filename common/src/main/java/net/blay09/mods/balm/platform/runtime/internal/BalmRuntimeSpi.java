package net.blay09.mods.balm.platform.runtime.internal;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

public class BalmRuntimeSpi {
    @SuppressWarnings("unchecked")
    public static BalmRuntime<BalmRuntimeLoadContext> create() {
        var loader = ServiceLoader.load(BalmRuntimeFactory.class);
        var factory = loader.findFirst().orElseThrow();
        return (BalmRuntime<@NotNull BalmRuntimeLoadContext>) factory.create();
    }
}
