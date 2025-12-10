package net.blay09.mods.balm.forge.platform.runtime.internal;

import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntimeFactory;

public class ForgeBalmRuntimeFactory implements BalmRuntimeFactory {
    @Override
    public BalmRuntime<?> create() {
        return new ForgeBalmRuntime();
    }
}
