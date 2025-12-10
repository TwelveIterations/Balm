package net.blay09.mods.balm.forge.platform.compatibility;

import net.blay09.mods.balm.forge.platform.compatibility.milk.ForgeBalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.hudinfo.internal.CommonBalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.BalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.CommonBalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.NoopTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.TrinketsMultiplexer;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;

import java.util.function.Supplier;

public class ForgeBalmModSupport implements BalmModSupport {
    private final Supplier<BalmModSupportTrinkets> trinkets;
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();
    private final CommonBalmModSupportRecipeViewer recipeViewers = new CommonBalmModSupportRecipeViewer();
    private final BalmModSupportMilkFluid milkFluid = new ForgeBalmModSupportMilkFluid();

    public ForgeBalmModSupport(BalmRuntime<?> runtime) {
        trinkets = runtime.<BalmModSupportTrinkets>modProxy()
                .withMultiplexer(TrinketsMultiplexer::new)
                .withFallback(new NoopTrinkets())
                .buildLazily();
    }

    @Override
    public BalmModSupportTrinkets trinkets() {
        return trinkets.get();
    }

    @Override
    public BalmModSupportHudInfo hudInfo() {
        return hudInfo;
    }

    @Override
    public BalmModSupportMilkFluid milkFluid() {
        return milkFluid;
    }

    @Override
    public BalmModSupportRecipeViewer recipeViewers() {
        return recipeViewers;
    }
}
