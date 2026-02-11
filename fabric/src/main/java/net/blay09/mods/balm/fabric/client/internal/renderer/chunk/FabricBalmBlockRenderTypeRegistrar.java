package net.blay09.mods.balm.fabric.client.internal.renderer.chunk;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class FabricBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {
    public static final FabricBalmBlockRenderTypeRegistrar INSTANCE = new FabricBalmBlockRenderTypeRegistrar();

    @Override
    public void setRenderLayer(Holder<Block> block, ChunkSectionLayer layer) {
    }

}
