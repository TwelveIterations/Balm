package net.blay09.mods.balm.api.compat;

import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.milk.BalmModSupportMilkFluid;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;

public interface BalmModSupport {

    BalmModSupportMilkFluid milkFluid();

    BalmModSupportTrinkets trinkets();

    BalmModSupportHudInfo hudInfo();
}
