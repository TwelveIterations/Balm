package net.blay09.mods.balm.fabric.client.renderer.chunk;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class FabricBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {
    public static final FabricBalmBlockRenderTypeRegistrar INSTANCE = new FabricBalmBlockRenderTypeRegistrar();

    @Override
    public void setRenderLayer(Supplier<Block> block, ChunkSectionLayer layer) {
        BlockRenderLayerMap.putBlocks(layer, block.get());
    }

}
