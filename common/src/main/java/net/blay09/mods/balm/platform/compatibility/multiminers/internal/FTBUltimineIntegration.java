package net.blay09.mods.balm.platform.compatibility.multiminers.internal;

import dev.ftb.mods.ftbultimine.api.FTBUltimineAPI;
import net.blay09.mods.balm.platform.compatibility.multiminers.BalmModSupportMultiMiners;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class FTBUltimineIntegration implements BalmModSupportMultiMiners {
    @Override
    public boolean isMultiMine(Player player, BlockPos pos) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return false;
        }

        return FTBUltimineAPI.api().currentBlockSelection(serverPlayer)
                .map(selection -> selection.contains(pos))
                .orElse(false);
    }
}
