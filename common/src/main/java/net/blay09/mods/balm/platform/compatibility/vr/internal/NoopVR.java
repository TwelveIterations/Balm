package net.blay09.mods.balm.platform.compatibility.vr.internal;

import net.blay09.mods.balm.platform.compatibility.vr.BalmModSupportVR;
import net.minecraft.world.entity.player.Player;

public class NoopVR implements BalmModSupportVR {
    @Override
    public boolean isInVR(Player player) {
        return false;
    }
}
