package net.blay09.mods.balm.platform.compatibility.multiminers.internal;

import net.blay09.mods.balm.platform.compatibility.multiminers.BalmModSupportMultiMiners;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class NoopMultiMiners implements BalmModSupportMultiMiners {
    @Override
    public boolean isMultiMine(Player player, BlockPos pos) {
        return false;
    }
}
