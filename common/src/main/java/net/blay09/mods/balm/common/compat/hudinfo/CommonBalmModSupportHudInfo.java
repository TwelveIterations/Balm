package net.blay09.mods.balm.common.compat.hudinfo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.hudinfo.BlockInfoProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CommonBalmModSupportHudInfo implements BalmModSupportHudInfo {

    private final List<BlockInfoProvider> globalBlockInfoProviders = new ArrayList<>();
    private final Multimap<Block, BlockInfoProvider> blockInfoProviders = ArrayListMultimap.create();

    @Override
    public void registerGlobalBlockInfo(ResourceLocation identifier, BlockInfoProvider provider) {
        globalBlockInfoProviders.add(provider);
    }

    @Override
    public void registerBlockInfo(ResourceLocation identifier, Block block, BlockInfoProvider provider) {
        blockInfoProviders.put(block, provider);
    }

    public List<BlockInfoProvider> getBlockInfoProviders(Block block) {
        final var result = new ArrayList<BlockInfoProvider>();
        result.addAll(blockInfoProviders.get(block));
        result.addAll(globalBlockInfoProviders);
        return result;
    }
}
