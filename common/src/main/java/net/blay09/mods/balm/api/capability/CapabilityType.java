package net.blay09.mods.balm.api.capability;

import net.minecraft.resources.ResourceLocation;

public record CapabilityType<TScope, TApi, TContext>(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass,
                                                     Object backingType) {
}
