package net.blay09.mods.balm.client.color.block.internal;

import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.world.level.block.BlockLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractBalmBlockColorRegistrar implements BalmBlockColorRegistrar {
    @Override
    public void register(BlockColor color, BlockLike... blocks) {
        register(color, Set.of(blocks));
    }

    @Override
    public void register(BlockColor color, Iterable<? extends BlockLike> blocks) {
        register(color, () -> {
            final var resolvedBlocks = new ArrayList<Block>();
            for (final var blockLike : blocks) {
                resolvedBlocks.add(blockLike.asBlock());
            }
            return resolvedBlocks.toArray(Block[]::new);
        });
    }
}
