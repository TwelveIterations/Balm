package net.blay09.mods.balm.forge.client.renderer.chunk;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class ForgeBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {

    @SuppressWarnings("removal")
    @Override
    public void setRenderLayer(Holder<Block> block, RenderType layer) {
        ItemBlockRenderTypes.setRenderLayer(block.value(), layer);
    }

}
