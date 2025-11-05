package net.blay09.mods.balm.client.renderer.chunk;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface BalmBlockRenderTypeRegistrar {
    default void setRenderLayer(BlockLike block, ChunkSectionLayer layer) {
        setRenderLayer(block::asBlock, layer);
    }

    void setRenderLayer(Supplier<Block> block, ChunkSectionLayer layer);
}