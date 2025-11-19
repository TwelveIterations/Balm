package net.blay09.mods.balm.fabric.platform.runtime;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;

public record FabricLoadContext() implements BalmRuntimeLoadContext {
    public static final FabricLoadContext INSTANCE = new FabricLoadContext();
}
