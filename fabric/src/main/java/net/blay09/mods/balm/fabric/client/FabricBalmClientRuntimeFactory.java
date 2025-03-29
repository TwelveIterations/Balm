package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.BalmClientRuntimeFactory;

public class FabricBalmClientRuntimeFactory implements BalmClientRuntimeFactory {
    @Override
    public BalmClientRuntime<?> create() {
        return new FabricBalmClientRuntime();
    }
}
