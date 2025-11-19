package net.blay09.mods.balm.neoforge.client.platform.runtime.internal;

import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntime;
import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntimeFactory;

public class NeoForgeBalmClientRuntimeFactory implements BalmClientRuntimeFactory {
    @Override
    public BalmClientRuntime<?> create() {
        return new NeoForgeBalmClientRuntime();
    }
}
