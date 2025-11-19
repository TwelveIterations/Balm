package net.blay09.mods.balm.platform.capabilities;

import net.minecraft.resources.Identifier;

public record CapabilityType<TScope, TApi, TContext>(Identifier identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass,
                                                     Object backingType) {
}
