package net.blay09.mods.balm.fabric.client.internal.renderer.chunk;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class FabricBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {
    public static final FabricBalmBlockRenderTypeRegistrar INSTANCE = new FabricBalmBlockRenderTypeRegistrar();

    @Override
    public void setRenderLayer(Holder<Block> block, RenderType layer) {
        BlockRenderLayerMap.INSTANCE.putBlocks(layer, block.value());
    }

}
