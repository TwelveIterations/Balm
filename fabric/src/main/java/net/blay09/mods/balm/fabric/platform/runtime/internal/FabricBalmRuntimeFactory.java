package net.blay09.mods.balm.fabric.platform.runtime.internal;

import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntimeFactory;

public class FabricBalmRuntimeFactory implements BalmRuntimeFactory {
    @Override
    public BalmRuntime<?> create() {
        return new FabricBalmRuntime();
    }
}
