package net.blay09.mods.balm.fabric.compat;

import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;
import net.blay09.mods.balm.fabric.compat.trinkets.FabricBalmModSupportTrinkets;

public class FabricBalmModSupport implements BalmModSupport {
    private final FabricBalmModSupportTrinkets trinkets = new FabricBalmModSupportTrinkets();
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();

    @Override
    public BalmModSupportTrinkets trinkets() {
        return trinkets;
    }

    @Override
    public BalmModSupportHudInfo hudInfo() {
        return hudInfo;
    }
}
