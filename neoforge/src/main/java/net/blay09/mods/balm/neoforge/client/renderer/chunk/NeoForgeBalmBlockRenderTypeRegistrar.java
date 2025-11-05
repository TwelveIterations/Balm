package net.blay09.mods.balm.neoforge.client.renderer.chunk;

import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

/**
 * NeoForge does not support changing block render layers at runtime; prefer specifying via model JSON ("render_type").
 * This registrar is a no-op to keep API parity.
 */
public class NeoForgeBalmBlockRenderTypeRegistrar implements BalmBlockRenderTypeRegistrar {

    @Override
    public void setRenderLayer(Holder<Block> block, ChunkSectionLayer layer) {
        ItemBlockRenderTypes.setRenderLayer(block.value(), layer);
    }

}
