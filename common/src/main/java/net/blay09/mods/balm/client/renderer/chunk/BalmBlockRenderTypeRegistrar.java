package net.blay09.mods.balm.client.renderer.chunk;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public interface BalmBlockRenderTypeRegistrar {
    default void setRenderLayer(DeferredBlock block, ChunkSectionLayer layer) {
        // We keep a DeferredBlock overload as it implements both BlockLike and Holder<Block> and would be ambiguous in the other overloads
        setRenderLayer(block.asHolder(), layer);
    }

    default void setRenderLayer(BlockLike block, ChunkSectionLayer layer) {
        setRenderLayer(block.asHolder(), layer);
    }

    void setRenderLayer(Holder<Block> block, ChunkSectionLayer layer);
}