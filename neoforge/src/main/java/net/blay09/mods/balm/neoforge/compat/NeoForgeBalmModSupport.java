package net.blay09.mods.balm.neoforge.compat;

import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.compat.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.balm.api.compat.trinkets.BalmModSupportTrinkets;
import net.blay09.mods.balm.common.compat.NoopTrinkets;
import net.blay09.mods.balm.common.compat.TrinketsMultiplexer;
import net.blay09.mods.balm.common.compat.hudinfo.CommonBalmModSupportHudInfo;

import java.util.function.Supplier;

public class NeoForgeBalmModSupport implements BalmModSupport {
    private final Supplier<BalmModSupportTrinkets> trinkets;
    private final CommonBalmModSupportHudInfo hudInfo = new CommonBalmModSupportHudInfo();

    public NeoForgeBalmModSupport(BalmRuntime<?> runtime) {
        trinkets = runtime.<BalmModSupportTrinkets>modProxy()
                .with("curios", "net.blay09.mods.balm.neoforge.compat.trinkets.CuriosIntegration")
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
}
