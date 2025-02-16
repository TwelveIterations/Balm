package net.blay09.mods.balm.forge.compat;

import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.forge.compat.trinkets.ForgeBalmModSupportTrinkets;

public class ForgeBalmModSupport implements BalmModSupport {
    private final ForgeBalmModSupportTrinkets trinkets = new ForgeBalmModSupportTrinkets();

    @Override
    public BalmModSupportTrinkets trinkets() {
        return trinkets;
    }
}
