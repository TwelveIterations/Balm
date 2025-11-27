package net.blay09.mods.balm.platform.compatibility;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.BalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;

public interface BalmModSupport {

    BalmModSupportMilkFluid milkFluid();

    BalmModSupportTrinkets trinkets();

    BalmModSupportHudInfo hudInfo();

    BalmModSupportRecipeViewer recipeViewers();
}
