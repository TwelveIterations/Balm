package net.blay09.mods.balm.client.renderer.chunk;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public interface BalmBlockRenderTypeRegistrar {
    default void setRenderLayer(BlockLike block, ChunkSectionLayer layer) {
        setRenderLayer(block.asHolder(), layer);
    }

    void setRenderLayer(Holder<Block> block, ChunkSectionLayer layer);
}