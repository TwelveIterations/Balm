package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeFactory;

public class ForgeBalmRuntimeFactory implements BalmRuntimeFactory {
    @Override
    public BalmRuntime<?> create() {
        return new ForgeBalmRuntime();
    }
}
