package net.blay09.mods.balm.client.color.block;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public interface BalmBlockColorRegistrar {

    void register(List<BlockTintSource> tintSources, BlockLike... blocks);

    void register(List<BlockTintSource> tintSources, Iterable<? extends BlockLike> blocks);

    void register(List<BlockTintSource> tintSources, Supplier<Block[]> blocks);
}
