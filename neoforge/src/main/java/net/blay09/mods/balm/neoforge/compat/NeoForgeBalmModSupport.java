package net.blay09.mods.balm.neoforge.compat;

import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.neoforge.compat.trinkets.NeoForgeBalmModSupportTrinkets;

public class NeoForgeBalmModSupport implements BalmModSupport {
    private final NeoForgeBalmModSupportTrinkets trinkets = new NeoForgeBalmModSupportTrinkets();

    @Override
    public BalmModSupportTrinkets trinkets() {
        return trinkets;
    }
}
