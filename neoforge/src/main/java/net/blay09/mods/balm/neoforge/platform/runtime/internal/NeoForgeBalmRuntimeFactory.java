package net.blay09.mods.balm.neoforge.platform.runtime.internal;

import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntimeFactory;

public class NeoForgeBalmRuntimeFactory implements BalmRuntimeFactory {
    @Override
    public BalmRuntime<?> create() {
        return new NeoForgeBalmRuntime();
    }
}
