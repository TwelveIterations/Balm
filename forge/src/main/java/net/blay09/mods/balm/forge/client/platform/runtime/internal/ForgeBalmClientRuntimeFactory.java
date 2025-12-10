package net.blay09.mods.balm.forge.client.platform.runtime.internal;

import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntime;
import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntimeFactory;

public class ForgeBalmClientRuntimeFactory implements BalmClientRuntimeFactory {
    @Override
    public BalmClientRuntime<?> create() {
        return new ForgeBalmClientRuntime();
    }
}
