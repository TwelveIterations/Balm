package net.blay09.mods.balm.platform.compatibility.multiminers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface BalmModSupportMultiMiners {
    boolean isMultiMine(Player player, BlockPos pos);
}
