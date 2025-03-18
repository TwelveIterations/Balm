package net.blay09.mods.balm.api.capability;

import net.minecraft.resources.ResourceLocation;

public record CapabilityType<TApi, TContext>(ResourceLocation identifier, Class<TApi> apiClass, Class<TContext> contextClass, Object backingType) {
}
