package net.blay09.mods.balm.neoforge.platform.compatibility.internal;

import net.blay09.mods.balm.neoforge.platform.compatibility.milk.internal.NeoForgeBalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.hudinfo.internal.CommonBalmModSupportHudInfo;
import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.blay09.mods.balm.platform.compatibility.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.NoopTrinkets;
import net.blay09.mods.balm.platform.compatibility.trinkets.internal.TrinketsMultiplexer;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;

import java.util.function.Supplier;

public class NeoForgeBalmModSupport implements BalmModSupport {
    private final Supplier<BalmModSupportTrinkets> trinkets;
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();
    private final BalmModSupportMilkFluid milkFluid = new NeoForgeBalmModSupportMilkFluid();

    public NeoForgeBalmModSupport(BalmRuntime<?> runtime) {
        trinkets = runtime.<BalmModSupportTrinkets>modProxy()
                .with("curios", "net.blay09.mods.balm.neoforge.platform.compatibility.trinkets.internal.CuriosIntegration")
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
}
