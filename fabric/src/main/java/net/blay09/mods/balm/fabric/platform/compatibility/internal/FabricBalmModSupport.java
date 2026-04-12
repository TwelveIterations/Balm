package net.blay09.mods.balm.fabric.platform.compatibility.internal;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.BalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.CommonBalmModSupportRecipeViewer;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.multiminers.BalmModSupportMultiMiners;
import net.blay09.mods.balm.platform.compatibility.multiminers.internal.MultiMinersMultiplexer;
import net.blay09.mods.balm.platform.compatibility.multiminers.internal.NoopMultiMiners;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.NoopTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.TrinketsMultiplexer;
import net.blay09.mods.balm.platform.compatibility.hudinfo.internal.CommonBalmModSupportHudInfo;
import net.blay09.mods.balm.fabric.platform.compatibility.milk.internal.FabricModSupportMilkFluid;

import java.util.function.Supplier;

public class FabricBalmModSupport implements BalmModSupport {
    private final Supplier<BalmModSupportMultiMiners> multiminers;
    private final Supplier<BalmModSupportTrinkets> trinkets;
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();
    private final CommonBalmModSupportRecipeViewer recipeViewers = new CommonBalmModSupportRecipeViewer();
    private final BalmModSupportMilkFluid milkFluid = new FabricModSupportMilkFluid();

    public FabricBalmModSupport(BalmRuntime<?> runtime) {
        multiminers = runtime.<BalmModSupportMultiMiners>modProxy()
                .with("ftbultimine", "net.blay09.mods.balm.platform.compatibility.multiminers.internal.FTBUltimineIntegration")
                .withMultiplexer(MultiMinersMultiplexer::new)
                .withFallback(new NoopMultiMiners())
                .buildLazily();
        trinkets = runtime.<BalmModSupportTrinkets>modProxy()
                .with("trinkets", "net.blay09.mods.balm.fabric.platform.compatibility.trinkets.internal.TrinketsIntegration")
                .withMultiplexer(TrinketsMultiplexer::new)
                .withFallback(new NoopTrinkets())
                .buildLazily();
    }

    @Override
    public BalmModSupportMilkFluid milkFluid() {
        return milkFluid;
    }

    @Override
    public BalmModSupportMultiMiners multiminers() {
        return multiminers.get();
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
    public BalmModSupportRecipeViewer recipeViewers() {
        return recipeViewers;
    }
}
