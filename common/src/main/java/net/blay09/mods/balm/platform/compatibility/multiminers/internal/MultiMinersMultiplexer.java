package net.blay09.mods.balm.platform.compatibility.multiminers.internal;

import net.blay09.mods.balm.platform.compatibility.multiminers.BalmModSupportMultiMiners;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class MultiMinersMultiplexer implements BalmModSupportMultiMiners {
    private final List<BalmModSupportMultiMiners> providers;

    public MultiMinersMultiplexer(List<BalmModSupportMultiMiners> providers) {
        this.providers = providers;
    }

    @Override
    public boolean isMultiMine(Player player, BlockPos pos) {
        return providers.stream().anyMatch(provider -> provider.isMultiMine(player, pos));
    }
}
