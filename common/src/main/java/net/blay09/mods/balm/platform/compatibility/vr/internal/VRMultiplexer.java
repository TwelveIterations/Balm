package net.blay09.mods.balm.platform.compatibility.vr.internal;

import net.blay09.mods.balm.platform.compatibility.vr.BalmModSupportVR;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class VRMultiplexer implements BalmModSupportVR {
    private final List<BalmModSupportVR> providers;

    public VRMultiplexer(List<BalmModSupportVR> providers) {
        this.providers = providers;
    }

    @Override
    public boolean isInVR(Player player) {
        return providers.stream().anyMatch(provider -> provider.isInVR(player));
    }
}
