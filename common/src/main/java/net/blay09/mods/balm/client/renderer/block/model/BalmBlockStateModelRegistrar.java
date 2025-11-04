package net.blay09.mods.balm.client.renderer.block.model;

import net.minecraft.resources.ResourceLocation;

public interface BalmBlockStateModelRegistrar {
    DeferredBlockStateModel register(ResourceLocation identifier);
}
