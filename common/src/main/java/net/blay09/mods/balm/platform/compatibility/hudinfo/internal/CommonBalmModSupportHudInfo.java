package net.blay09.mods.balm.platform.compatibility.hudinfo.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CommonBalmModSupportHudInfo implements BalmModSupportHudInfo {

    private final List<BlockInfoProvider> globalBlockInfoProviders = Collections.synchronizedList(new ArrayList<>());
    private final List<Pair<Supplier<Block>, BlockInfoProvider>> deferredBlockInfoProviders = Collections.synchronizedList(new ArrayList<>());
    private final Multimap<Block, BlockInfoProvider> blockInfoProviders = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

    @Override
    public void registerGlobalBlockInfo(Identifier identifier, BlockInfoProvider provider) {
        globalBlockInfoProviders.add(provider);
    }

    @Override
    public void registerBlockInfo(Identifier identifier, Supplier<Block> block, BlockInfoProvider provider) {
        deferredBlockInfoProviders.add(Pair.of(block, provider));
    }

    public List<BlockInfoProvider> getBlockInfoProviders(Block block) {
        for (final var deferredBlockInfoProvider : deferredBlockInfoProviders) {
            blockInfoProviders.put(deferredBlockInfoProvider.getFirst().get(), deferredBlockInfoProvider.getSecond());
        }
        deferredBlockInfoProviders.clear();

        final var result = new ArrayList<BlockInfoProvider>();
        result.addAll(blockInfoProviders.get(block));
        result.addAll(globalBlockInfoProviders);
        return result;
    }
}
