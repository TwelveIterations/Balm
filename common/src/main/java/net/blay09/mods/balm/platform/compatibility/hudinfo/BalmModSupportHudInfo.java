package net.blay09.mods.balm.platform.compatibility.hudinfo;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface BalmModSupportHudInfo {
    void registerGlobalBlockInfo(Identifier identifier, BlockInfoProvider provider);

    default void registerBlockInfo(Identifier identifier, Block block, BlockInfoProvider provider) {
        registerBlockInfo(identifier, () -> block, provider);
    }

    default void registerBlockInfo(Identifier identifier, DeferredBlock block, BlockInfoProvider provider) {
        registerBlockInfo(identifier, block::asBlock, provider);
    }

    void registerBlockInfo(Identifier identifier, Supplier<Block> block, BlockInfoProvider provider);

}
