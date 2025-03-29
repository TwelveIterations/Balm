package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeFactory;

public class FabricBalmRuntimeFactory implements BalmRuntimeFactory {
    @Override
    public BalmRuntime<?> create() {
        return new FabricBalmRuntime();
    }
}
