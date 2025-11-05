package net.blay09.mods.balm.client.color.block;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface BalmBlockColorRegistrar {

    void register(BlockColor color, BlockLike... blocks);

    void register(BlockColor color, Iterable<? extends BlockLike> blocks);

    void register(BlockColor color, Supplier<Block[]> blocks);
}
