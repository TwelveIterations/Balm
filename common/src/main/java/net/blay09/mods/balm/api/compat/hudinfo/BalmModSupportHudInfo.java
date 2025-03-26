package net.blay09.mods.balm.api.compat.hudinfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface BalmModSupportHudInfo {
    void registerGlobalBlockInfo(ResourceLocation identifier, BlockInfoProvider provider);

    void registerBlockInfo(ResourceLocation identifier, Block block, BlockInfoProvider provider);
}
