package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.BalmRuntimeFactory;

public class NeoForgeBalmRuntimeFactory implements BalmRuntimeFactory {
    @Override
    public BalmRuntime<?> create() {
        return new NeoForgeBalmRuntime();
    }
}
