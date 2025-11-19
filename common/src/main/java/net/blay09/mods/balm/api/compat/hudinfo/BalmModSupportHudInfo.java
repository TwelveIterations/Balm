package net.blay09.mods.balm.api.compat.hudinfo;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public interface BalmModSupportHudInfo {
    void registerGlobalBlockInfo(Identifier identifier, BlockInfoProvider provider);

    void registerBlockInfo(Identifier identifier, Block block, BlockInfoProvider provider);
}
