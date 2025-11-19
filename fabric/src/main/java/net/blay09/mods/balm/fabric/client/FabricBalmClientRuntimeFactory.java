package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntime;
import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntimeFactory;

public class FabricBalmClientRuntimeFactory implements BalmClientRuntimeFactory {
    @Override
    public BalmClientRuntime<?> create() {
        return new FabricBalmClientRuntime();
    }
}
