package net.blay09.mods.balm.forge.client.renderer.chunk;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ForgeBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {

    @Override
    public void setRenderLayer(Supplier<Block> block, ChunkSectionLayer layer) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), layer);
    }

}
