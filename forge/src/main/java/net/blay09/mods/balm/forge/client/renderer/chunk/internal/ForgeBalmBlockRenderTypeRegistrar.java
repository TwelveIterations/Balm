package net.blay09.mods.balm.forge.client.renderer.chunk.internal;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class ForgeBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {

    @Override
    public void setRenderLayer(Holder<Block> block, ChunkSectionLayer layer) {
        ItemBlockRenderTypes.setRenderLayer(block.value(), layer);
    }

}
